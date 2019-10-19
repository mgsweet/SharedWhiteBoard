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
			JSONObject resJson = new JSONObject();
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
				int resID = controler.getRoomManager().addRoom(ipAddress, port, hostName, roomName, password);
				controler.printOnBoth(
						hostName + " create a room! Current room num: " + controler.getRoomManager().getRoomNum());
				controler.printOnBoth("- Host: " + ipAddress + ": " + port);
				resJson.put("state", StateCode.SUCCESS);
				resJson.put("roomId", resID);
				break;
			case StateCode.REMOVE_ROOM:
				roomId = Integer.parseInt(reqJSON.get("roomId").toString());
				int state = controler.getRoomManager().removeRoom(roomId);
				resJson.put("state", String.valueOf(state));
				break;
			case StateCode.GET_ROOM_LIST:
				Map<Integer, String> roomList = controler.getRoomManager().getRoomList();
				resJson.put("roomList", roomList);
				System.out.println("A client request for roomlist info.");
				break;
			case StateCode.GET_ROOM_INFO:
				roomId = reqJSON.getInteger("roomId");
				password = reqJSON.getString("password");
				if (controler.getRoomManager().checkRoomPassword(roomId, password)) {
					System.out.println("Password Correct!");
					Room room = controler.getRoomManager().getRoomInfo(roomId);
					resJson.put("state", String.valueOf(StateCode.SUCCESS));
					resJson.put("ip", room.getIpAddress());
					resJson.put("port", room.getPort());
				} else {
					resJson.put("state", String.valueOf(StateCode.FAIL));
				}
				break;
			case StateCode.ADD_USER:
				userId = reqJSON.get("userId").toString();
				if (controler.getUserlist().containsKey(userId)) {
					controler.printOnBoth("A user try to join but " + userId + " exist!");
					resJson.put("state", String.valueOf(StateCode.FAIL));
				} else {
					controler.getUserlist().put(userId, userId);
					controler.printOnBoth(
							"User-" + userId + " join. " + "Current user number: " + controler.getUserlist().size());
					resJson.put("state", String.valueOf(StateCode.SUCCESS));
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
					resJson.put("state", String.valueOf(StateCode.SUCCESS));
				} else {
					controler.printOnBoth(userId + " not exist! Can't be delete.");
					resJson.put("state", String.valueOf(StateCode.FAIL));
				}
				break;
			default:
				System.out.print("Err: Unknown Command: " + command);
				break;
			}
			// Send back to client
			writer.writeUTF(resJson.toJSONString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
