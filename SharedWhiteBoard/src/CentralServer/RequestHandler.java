/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package CentralServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import StateCode.StateCode;

public class RequestHandler extends Thread {
	private Socket clientSocket;
	private RoomManager rm;
	
	public RequestHandler(Socket clientSocket, RoomManager rm) {
		this.clientSocket = clientSocket;
		this.rm = rm;
	}
	
	public void run() {
		try {
			DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
			JSONObject reqJSON = parseReqString(reader.readUTF());
			int command = Integer.parseInt(reqJSON.get("command").toString());
			JSONObject requestJson = new JSONObject();
			// excute command
			String password = "";
			int roomId = -1;
			switch (command) {
			case StateCode.ADD_ROOM:
				// get info from request
				String ipAddress = reqJSON.get("ipAddress").toString();
				int port = Integer.parseInt(reqJSON.get("port").toString());
				String hostName = reqJSON.get("hostName").toString();
				String roomName = reqJSON.get("roomName").toString();
				password = reqJSON.get("password").toString();
				// return roomID
				int reqID = rm.addRoom(ipAddress, port, hostName, roomName, password);
				requestJson.put("roomID", String.valueOf(reqID));
				break;
			case StateCode.REMOVE_ROOM:
				roomId = Integer.parseInt(reqJSON.get("roomID").toString());
				int opeartionState = rm.removeRoom(roomId);
				requestJson.put("opeartionState", String.valueOf(opeartionState));
				break;
			case StateCode.GET_ROOMS_LIST: 
				Map<Integer, String> roomsList = rm.getRoomsList();
				requestJson.put("roomsList", roomsList);
				System.out.println("A client request for roomlist info.");
				break;
			case StateCode.GET_ROOM_INFO:
				roomId = Integer.parseInt(reqJSON.get("roomID").toString());
				password = reqJSON.get("password").toString();
				if (rm.checkRoomPassword(roomId, password)) {
					Room room = rm.getRoomInfo(roomId);
					requestJson.put("opeartionState", String.valueOf(StateCode.SUCCESS));
					requestJson.put("roomInfo", room);
				} else {
					requestJson.put("opeartionState", String.valueOf(StateCode.FAIL));
				}
				break;
			default:
				System.out.print("Err: Unknown Command: " + command);
				break;
			}
			// Send back to client
			writer.writeUTF(requestJson.toJSONString());
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
	
	private JSONObject createResJSON(int state, String meaning) {
		JSONObject requestJson = new JSONObject();
		requestJson.put("state", String.valueOf(state));
		requestJson.put("meaning", meaning);
		return requestJson;
	}
	
}
