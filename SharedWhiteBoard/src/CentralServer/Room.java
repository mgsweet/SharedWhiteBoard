/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package CentralServer;

public class Room {
	private int roomId;
	private String ipAddress;
	private int port;
	private String hostName;
	private String roomName;
	private String password;
	
	public Room(int roomId, String ipAddress, int port, String hostName, String roomName, String password) {
		this.roomId = roomId;
		this.ipAddress = ipAddress;
		this.port = port;
		this.hostName = hostName;
		this.roomName = roomName;
		this.password = password;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public int getPort() {
		return port;
	}
	
	
	
}
