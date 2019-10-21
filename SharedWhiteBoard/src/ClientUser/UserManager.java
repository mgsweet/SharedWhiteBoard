package ClientUser;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import RMI.IRemoteApp;
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
	private IRemoteUM hostRemoteUM;
	private IRemoteApp hostRemoteApp;

	// Guests info
	private Map<String, User> guests;
	private Map<String, IRemotePaint> guestRemotePaints;
	private Map<String, IRemoteApp> guestRemoteApps;
	private Map<String, IRemoteUM> guestRemoteUMs;

	// Visitors info
	private Map<String, User> visitors;
	private Map<String, IRemoteApp> visitorRemoteApps;

	// Use to refresh ui
	ClientListScrollPanel clsp;

	public UserManager(Boolean isHost, String hostId, String hostIp, int registerPort, int chatPort) {
		this.isHost = isHost;

		this.host = new User(hostId, User.HOST, hostIp, registerPort, chatPort);
		if (!isHost) {
			try {
				Registry registry = LocateRegistry.getRegistry(hostIp, registerPort);
				hostRemotePaint = (IRemotePaint) registry.lookup("paint");
				hostRemoteUM = (IRemoteUM) registry.lookup("um");
				hostRemoteApp = (IRemoteApp) registry.lookup("app");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		guests = new HashMap<String, User>();
		guestRemotePaints = new HashMap<String, IRemotePaint>();
		guestRemoteUMs = new HashMap<String, IRemoteUM>();
		guestRemoteApps = new HashMap<String, IRemoteApp>();

		visitors = new HashMap<String, User>();
		visitorRemoteApps = new HashMap<String, IRemoteApp>();
	}

	/**
	 * Check whether is the user manager belongs to host.
	 * 
	 * @return
	 */
	public Boolean isHost() {
		return isHost;
	}

	/**
	 * Get remote UM.
	 * 
	 * @return
	 */
	public IRemoteUM getHostRemoteUM() {
		return hostRemoteUM;
	}

	/**
	 * Add a visitor to guest list, open the door for a visitor.
	 * 
	 * @param guestId
	 */
	public void addGuest(String guestId) {
		// add guest
		User guest = visitors.get(guestId);
		guests.put(guestId, guest);
		guestRemoteApps.put(guestId, visitorRemoteApps.get(guestId));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(guest.getIp(), guest.getRegisterPort());
			IRemoteApp guestRemoteApp = (IRemoteApp) clientRegistry.lookup("app");
			guestRemoteApp.askIn(host.getUserId(), host.getIp(), host.getRegisterPort());

			IRemotePaint remotePaint = (IRemotePaint) clientRegistry.lookup("paint");
			guestRemotePaints.put(guestId, remotePaint);

			IRemoteUM remoteUM = (IRemoteUM) clientRegistry.lookup("um");
			guestRemoteUMs.put(guestId, remoteUM);

			// delete visitor
			visitors.remove(guestId);
			visitorRemoteApps.remove(guestId);

			// set remote user manager
			for (IRemoteUM x : guestRemoteUMs.values()) {
				x.setGuests(guests);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
	}

	/**
	 * Remove a guest from guest list.
	 * 
	 * @param gusetId
	 */
	public void removeGuest(String guestId) {
		try {
			IRemoteApp remoteApp = guestRemoteApps.get(guestId);
			remoteApp.askOut();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO
		}
		guests.remove(guestId);
		guestRemoteUMs.remove(guestId);
		guestRemotePaints.remove(guestId);
		guestRemoteApps.remove(guestId);
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
	 * 
	 * @param userId
	 * @param ip
	 * @param registerPort
	 */
	public void addVistor(String userId, String ip, int registerPort) {
		visitors.put(userId, new User(userId, User.VISTOR, ip, registerPort, -1));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(ip, registerPort);
			IRemoteApp remoteVistorApp = (IRemoteApp) clientRegistry.lookup("app");
			visitorRemoteApps.put(userId, remoteVistorApp);
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
	 * 
	 * @param userId
	 */
	public void removeVistor(String userId) {
		visitors.remove(userId);
		visitorRemoteApps.remove(userId);

		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
	}
	
	public void kickVisitor(String userId) {
		try {
			IRemoteApp remoteApp = visitorRemoteApps.get(userId);
			remoteApp.askOut();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO
		}
		
		removeVistor(userId);
	}

	/**
	 * Get guset list.
	 * 
	 * @return
	 */
	public Map<String, User> getGuests() {
		return guests;
	}

	/**
	 * Get visitor list.
	 * 
	 * @return
	 */
	public Map<String, User> getVisitors() {
		return visitors;
	}

	/**
	 * Get host.
	 * 
	 * @return
	 */
	public User getHost() {
		return host;
	}

	/**
	 * Get the remotePaint of host
	 * 
	 * @return
	 */
	public IRemotePaint getHostRemotePaint() {
		return hostRemotePaint;
	}

	/**
	 * Set the clientListScrollPanel.
	 * 
	 * @param clsp
	 */
	public void setCLSP(ClientListScrollPanel clsp) {
		this.clsp = clsp;
	}

	/**
	 * Get the identity of a given userId.
	 * 
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
	 * 
	 * @param guests
	 */
	public void setGuests(Map<String, User> guests) {
		this.guests = guests;
		// refresh ui.
		if (clsp != null) {
			clsp.updateUserList();
		}
	}
}
