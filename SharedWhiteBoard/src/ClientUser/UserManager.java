package ClientUser;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import RMI.IRemotePaint;
import RMI.IRemoteUM;
import WhiteBoard.ClientListScrollPanel;

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
	private Map<String, User> guests;
	private Map<String, IRemotePaint> guestRemotePaints;
	private Map<String, IRemoteUM> guestRemoteUMs;

	// Visitors info
	private Map<String, User> visitors;
	private Map<String, IRemoteUM> visitorUMs;
	
	// Use to refresh ui
	ClientListScrollPanel clsp;

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
		
		guests = new HashMap<String, User>();
		guestRemotePaints = new HashMap<String, IRemotePaint>();
		guestRemoteUMs = new HashMap<String, IRemoteUM>();

		visitors = new HashMap<String, User>();
		visitorUMs = new HashMap<String, IRemoteUM>();
	}
	
	public Boolean isHost() {
		return isHost;
	}
	
	/**
	 * Add a visitor to guest list, open the door for a visitor.
	 * @param guestId
	 */
	public void addGuest(String guestId) {
		// add guest
		User guest = visitors.get(guestId);
		guests.put(guestId, guest);
		guestRemoteUMs.put(guestId, visitorUMs.get(guestId));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(guest.getIp(), guest.getRegisterPort());
			IRemoteUM guestRemoteUM = (IRemoteUM) clientRegistry.lookup("umRMI");
			guestRemoteUM.askIn(host.getUserId(), host.getIp(), host.getRegisterPort());
			IRemotePaint remotePaint = (IRemotePaint) clientRegistry.lookup("paintRMI");
			guestRemotePaints.put(guestId, remotePaint);
		} catch (Exception e) {
			System.out.println("Can not get the client registry.");
			e.printStackTrace();
		}
		// delete visitor
		removeVistor(guestId);
		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
	}
	
	/**
	 * Remove a guest from guest list.
	 * @param gusetId
	 */
	public void removeGuest(String guestId) {
		guests.remove(guestId);
		guestRemoteUMs.remove(guestId);
		guestRemotePaints.remove(guestId);
		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
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
		visitors.put(userId, new User(userId, User.VISTOR, ip, registerPort, -1));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(ip, registerPort);
			IRemoteUM remoteVistor = (IRemoteUM) clientRegistry.lookup("umRMI");
			visitorUMs.put(userId, remoteVistor);
		} catch (Exception e) {
			System.out.println("Can not get the client registry.");
			e.printStackTrace();
		}
		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
	}
	
	/**
	 * Remove the visitor from the visitor list.
	 * @param userId
	 */
	public void removeVistor(String userId) {
		visitors.remove(userId);
		visitorUMs.remove(userId);
		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
	}
	
	/**
	 * Get guset list.
	 * @return
	 */
	public Map<String, User>  getGuests() {
		return guests;
	}
	
	/**
	 * Get visitor list.
	 * @return
	 */
	public Map<String, User> getVisitors() {
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
	
	/**
	 * Set the clientListScrollPanel.
	 * @param clsp
	 */
	public void setCLSP(ClientListScrollPanel clsp) {
		this.clsp = clsp;
	}
	
	/**
	 * Get the identity of a given userId.
	 * @param userId
	 * @return
	 */
	public int getIdentity(String userId) {
		if (host.getUserId().equals(userId)) {
			return User.HOST;
		} else if (guests.containsKey(userId)) {
			return User.GUEST;
		} else {
			return User.VISTOR;
		}
	}
	
	/**
	 * Update all the guests.
	 * @param guests
	 */
	public void setGuests(Map<String, User> guests) {
		this.guests = guests;
	}
	
}
