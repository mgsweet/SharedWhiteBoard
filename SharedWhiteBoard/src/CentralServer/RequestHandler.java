package CentralServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import StateCode.StateCode;

/**
 * Thread per request structure.
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
			JSONObject reqJSON = parseReqString(reader.readUTF());
			int command = Integer.parseInt(reqJSON.get("command").toString());
			JSONObject resJson = new JSONObject();
			// Execute command
			String password = "";
			int roomId = -1;
			switch (command) {
			case StateCode.ADD_ROOM:
				// get info from request
				String roomName = reqJSON.get("roomName").toString();
				password = reqJSON.get("password").toString();
				String hostName = reqJSON.get("hostName").toString();
				String ipAddress = reqJSON.get("hostIp").toString();
				int port = (int) reqJSON.get("hostPort");
				// return roomID
				int resID = controler.getRoomManager().addRoom(ipAddress, port, hostName, roomName, password);
				controler.printOnBoth(hostName + " create a room! Current room num: " + controler.getRoomManager().getRoomNum());
				resJson.put("state", StateCode.SUCCESS);
				resJson.put("roomID", String.valueOf(resID));
				break;
			case StateCode.REMOVE_ROOM:
				roomId = Integer.parseInt(reqJSON.get("roomID").toString());
				int state = controler.getRoomManager().removeRoom(roomId);
				resJson.put("state", String.valueOf(state));
				break;
			case StateCode.GET_ROOM_LIST:
				Map<Integer, String> roomList = controler.getRoomManager().getRoomList();
				resJson.put("roomList", roomList);
				System.out.println("A client request for roomlist info.");
				break;
			case StateCode.GET_ROOM_INFO:
				roomId = Integer.parseInt(reqJSON.get("roomID").toString());
				password = reqJSON.get("password").toString();
				if (controler.getRoomManager().checkRoomPassword(roomId, password)) {
					Room room = controler.getRoomManager().getRoomInfo(roomId);
					resJson.put("state", String.valueOf(StateCode.SUCCESS));
					resJson.put("roomInfo", room);
				} else {
					resJson.put("state", String.valueOf(StateCode.FAIL));
				}
				break;
			case StateCode.ADD_USER:
				String userId = reqJSON.get("userId").toString();
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

	private JSONObject parseReqString(String res) {
		JSONObject reqJSON = null;
		try {
			JSONParser parser = new JSONParser();
			reqJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqJSON;
	}
}
