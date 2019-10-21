package ClientUser;

import java.io.Serializable;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 11:51:11 AM
 */

public class User implements Serializable {
	public static final int HOST = 0;
	public static final int GUEST = 1;
	public static final int VISTOR = 2;
	// Personal info
	String userId;
	
	// User identity
	int identity;

	// Network Info
	String ip;
	int registerPort;
	int chatPort;

	public User(String userId, int identity, String ip, int registerPort, int chatPort) {
		this.userId = userId;
		this.identity = identity;
		this.ip = ip;
		this.registerPort = registerPort;
		this.chatPort = chatPort;
	}

	public String getUserId() {
		return userId;
	}

	public String getIp() {
		return ip;
	}

	public int getRegisterPort() {
		return registerPort;
	}

	public int getChatPort() {
		return chatPort;
	}
}
