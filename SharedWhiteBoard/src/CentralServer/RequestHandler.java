package CentralServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import StateCode.StateCode;

/**
 * Thread per request structure.
 * 
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 11:59:52 AM
 */

public class RequestHandler extends Thread {
	private Socket clientSocket;
	CentralServer controler;

	public RequestHandler(Socket clientSocket, CentralServer controler) {
		this.clientSocket = clientSocket;
		this.controler = controler;
	}

	public void run() {
		try {
			DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
			JSONObject reqJSON = JSON.parseObject(reader.readUTF());
			int command = Integer.parseInt(reqJSON.get("command").toString());
			JSONObject resJSON = new JSONObject();
			// Execute command
			String password = "";
			int roomId = -1;
			String userId = "";

			switch (command) {
			case StateCode.ADD_ROOM:
				// get info from request
				String roomName = reqJSON.getString("roomName");
				password = reqJSON.getString("password");
				String hostName = reqJSON.getString("hostName");
				String ipAddress = reqJSON.getString("hostIp");
				int port = reqJSON.getInteger("hostPort");
				// return roomId
				roomId = controler.getRoomManager().addRoom(ipAddress, port, hostName, roomName, password);
				controler.printOnBoth(
						hostName + " create a room! Current room num: " + controler.getRoomManager().getRoomNum());
				controler.printOnBoth("- Host: " + ipAddress + ": " + port);
				resJSON.put("state", StateCode.SUCCESS);
				resJSON.put("roomId", roomId);
				break;
			case StateCode.REMOVE_ROOM:
				roomId = Integer.parseInt(reqJSON.get("roomId").toString());
				int state = controler.getRoomManager().removeRoom(roomId);
				resJSON.put("state", String.valueOf(state));
				controler.printOnBoth("Room: " + roomId + " is removed.");
				break;
			case StateCode.GET_ROOM_LIST:
				Map<Integer, String> roomList = controler.getRoomManager().getRoomList();
				resJSON.put("roomList", roomList);
				System.out.println("A client request for roomlist info.");
				break;
			case StateCode.GET_ROOM_INFO:
				roomId = reqJSON.getInteger("roomId");
				password = reqJSON.getString("password");
				if (controler.getRoomManager().checkRoomPassword(roomId, password)) {
					System.out.println("Password Correct!");
					Room room = controler.getRoomManager().getRoomInfo(roomId);
					resJSON.put("hostId", room.getHostName());
					resJSON.put("state", StateCode.SUCCESS);
					resJSON.put("ip", room.getIpAddress());
					resJSON.put("port", room.getPort());
					controler.printOnBoth("A client asks room " + roomId + " with correct password.");
				} else {
					resJSON.put("state", StateCode.FAIL);
					controler.printOnBoth("A client asks room " + roomId + " with worng password.");
				}
				break;
			case StateCode.ADD_USER:
				userId = reqJSON.get("userId").toString();
				if (controler.getUserlist().containsKey(userId)) {
					controler.printOnBoth("A user try to join but " + userId + " exist!");
					resJSON.put("state", StateCode.FAIL);
				} else {
					controler.getUserlist().put(userId, userId);
					controler.printOnBoth(
							"User-" + userId + " join. " + "Current user number: " + controler.getUserlist().size());
					resJSON.put("state", StateCode.SUCCESS);
				}
				break;
			case StateCode.REMOVE_USER:
				userId = reqJSON.get("userId").toString();
				if (controler.getUserlist().containsKey(userId)) {
					controler.getUserlist().remove(userId);
					controler.printOnBoth(
							"Delete " + userId + ". " + "Current user number: " + controler.getUserlist().size());
					controler.getRoomManager().removeRoom(userId);
					controler.printOnBoth("Delete " + userId + "'s room. " + "Current user number: "
							+ controler.getRoomManager().getRoomNum());
					resJSON.put("state", StateCode.SUCCESS);
				} else {
					controler.printOnBoth(userId + " not exist! Can't be delete.");
					resJSON.put("state", StateCode.FAIL);
				}
				break;
			default:
				System.out.print("Err: Unknown Command: " + command);
				break;
			}
			// Send back to client
			writer.writeUTF(resJSON.toJSONString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
