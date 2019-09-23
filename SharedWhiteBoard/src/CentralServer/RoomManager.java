/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package CentralServer;

import java.util.HashMap;
import java.util.Map;

import StateCode.StateCode;

public class RoomManager {
	private Map<Integer, Room> rooms;
	private int nextID;
	private int updateTimes;
	
	public RoomManager() {
		rooms = new HashMap<Integer, Room> ();
		nextID = 0;
		updateTimes = 0;
	}
	
	public synchronized int addRoom(String ipAddress, int port, String hostName, String roomName, String password) {
		Room room = new Room(nextID, ipAddress, port, hostName, roomName, password);
		rooms.put(nextID, room);
		nextID += 1;
		return nextID - 1;
	}
	
	public synchronized Map<Integer, String> getRoomsList() {
		Map<Integer, String> roomsList = new HashMap<Integer, String>();
		roomsList.put(0, "RoomName0 HostName0");
		roomsList.put(1, "RoomName1 HostName1");
		roomsList.put(2, "RoomName2 HostName2");
//		roomsList.put(3, "RoomName3 HostName3");
//		roomsList.put(4, "RoomName4 HostName4");
//		roomsList.put(5, "RoomName5 HostName5");
//		roomsList.put(6, "RoomName6 HostName6");
//		roomsList.put(7, "RoomName7 HostName7");
		return roomsList;
		
//		Map<Integer, String> reqRooms = new HashMap<Integer, String>();
//		for (Map.Entry<Integer, Room> entry: rooms.entrySet()) {
//			Room room = entry.getValue();
//			reqRooms.put(entry.getKey(), room.getRoomName() + ' ' + room.getHostName());
//		}
//		return reqRooms;
	}
	
	public synchronized int removeRoom(int roomId) {
		if (rooms.containsKey(roomId)) {
			rooms.remove(roomId);
			return StateCode.SUCCESS;
		} else {
			return StateCode.FAIL;
		}
	}
	
	public synchronized boolean checkRoomPassword(int roomId, String password) {
		if (rooms.containsKey(roomId)) {
			return password == rooms.get(roomId).getPassword();
		} else {
			return false;
		}
	}
	
	public synchronized Room getRoomInfo(int roomID) {
		return rooms.get(roomID);
	}
	
	public synchronized int getUpdateTimes() {
		return updateTimes;
	}
}
