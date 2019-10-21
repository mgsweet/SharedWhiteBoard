package ClientUser;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import RMI.IRemotePaint;
import RMI.IRemoteUM;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 11:59:41 AM
 */

public class UserManager {
	private Boolean isHost;
	
	// Host info
	private User host;
	private IRemotePaint hostRemotePaint;

	// Guests info
	private Map<String, User> guestList;
	private Map<String, IRemotePaint> guestRemotePaints;
	private Map<String, IRemoteUM> guestRemoteUMs;

	// Visitors info
	private Map<String, User> visitorList;
	private Map<String, IRemoteUM> visitorUMs;

	public UserManager(Boolean isHost, String hostId, String hostIp, int registerPort, int chatPort) {
		this.isHost = isHost;
		
		this.host = new User(hostId, User.HOST, hostIp, registerPort, chatPort);
		if (!isHost) {
			try {
				Registry registry = LocateRegistry.getRegistry(hostIp, registerPort);
				hostRemotePaint = (IRemotePaint) registry.lookup("paintRMI");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		guestList = new HashMap<String, User>();
		guestRemotePaints = new HashMap<String, IRemotePaint>();
		guestRemoteUMs = new HashMap<String, IRemoteUM>();

		visitorList = new HashMap<String, User>();
		visitorUMs = new HashMap<String, IRemoteUM>();
	}
	
	/**
	 * Add a visitor to guest list, open the door for a visitor.
	 * @param guestId
	 */
	public void addGuest(String guestId) {
		// add guest
		User guest = visitorList.get(guestId);
		guestList.put(guestId, guest);
		guestRemoteUMs.put(guestId, visitorUMs.get(guestId));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(guest.getIp(), guest.getRegisterPort());
			IRemotePaint remotePaint = (IRemotePaint) clientRegistry.lookup("paintRMI");
			guestRemotePaints.put(guestId, remotePaint);
		} catch (Exception e) {
			System.out.println("Can not get the client registry.");
			e.printStackTrace();
		}
		// delete visitor
		removeVistor(guestId);
	}
	
	/**
	 * Remove a guest from guest list.
	 * @param gusetId
	 */
	public void removeGuest(String guestId) {
		guestList.remove(guestId);
		guestRemoteUMs.remove(guestId);
		guestRemotePaints.remove(guestId);
	}
	
	public Map<String, IRemotePaint> getGuestRemotePaints() {
		return guestRemotePaints;
	}
	
	/**
	 * Add a visitor to the visitor list, when the visitor knock the door.
	 * @param userId
	 * @param ip
	 * @param registerPort
	 */
	public void addVistor(String userId, String ip, int registerPort) {
		visitorList.put(userId, new User(userId, User.VISTOR, ip, registerPort, -1));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(ip, registerPort);
			IRemoteUM remoteVistor = (IRemoteUM) clientRegistry.lookup("umRMI");
			visitorUMs.put(userId, remoteVistor);
		} catch (Exception e) {
			System.out.println("Can not get the client registry.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove the visitor from the visitor list.
	 * @param userId
	 */
	public void removeVistor(String userId) {
		visitorList.remove(userId);
		visitorUMs.remove(userId);
	}
	
	/**
	 * Get guset list.
	 * @return
	 */
	public Vector<User> getGuestList() {
		Vector<User> guest = new Vector<User>(guestList.values());
		return guest;
	}
	
	/**
	 * Get visitor list.
	 * @return
	 */
	public Vector<User> getVisitorList() {
		Vector<User> visitors = new Vector<User>(visitorList.values());
		return visitors;
	}
	
	/**
	 * Get host.
	 * @return
	 */
	public User getHost() {
		return host;
	}
	
	/**
	 * Get the remotePaint of host
	 * @return
	 */
	public IRemotePaint getHostRemotePaint() {
		return hostRemotePaint;
	}
	
}
