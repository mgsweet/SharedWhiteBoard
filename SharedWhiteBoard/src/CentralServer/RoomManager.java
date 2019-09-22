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
	
	public synchronized Map getRoomsList() {
		Map reqRooms = new HashMap();
		for (Map.Entry<Integer, Room> entry: rooms.entrySet()) {
			Room room = entry.getValue();
			reqRooms.put(entry.getKey(), room.getRoomName() + ' ' + room.getHostName());
		}
		return reqRooms;
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
