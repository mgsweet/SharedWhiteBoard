package CentralServer;

/**
 * In this version, the programme would not use this class.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 3:46:11 PM
 */

public class UserInfo {
	private String name;
	private String ip;
	private int port;
	
	public UserInfo(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
}


