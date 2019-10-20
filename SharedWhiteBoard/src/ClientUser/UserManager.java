package ClientUser;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import RMI.IRemotePaint;
import RMI.IRemoteVistor;

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

	// Visitors info
	private Map<String, User> visitorList;
	private Map<String, IRemoteVistor> remoteVistors;

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

		visitorList = new HashMap<String, User>();
		remoteVistors = new HashMap<String, IRemoteVistor>();
	}

	public void addVistor(String userId, String ip, int registerPort) {
		visitorList.put(userId, new User(userId, User.VISTOR, ip, registerPort, -1));
		try {
			Registry clientRegistry = LocateRegistry.getRegistry(ip, registerPort);
			IRemoteVistor remoteVistor = (IRemoteVistor) clientRegistry.lookup("vistor");
			remoteVistors.put(userId, remoteVistor);
		} catch (Exception e) {
			System.out.println("Can not get the client registry.");
			e.printStackTrace();
		}
	}
	
	public void removeVistor(String userId) {
		visitorList.remove(userId);
		remoteVistors.remove(userId);
	}
	
	public Vector<User> getUserList() {
		Vector<User> users = new Vector<User>(guestList.values());
		users.add(host);
		return users;
	}
}
