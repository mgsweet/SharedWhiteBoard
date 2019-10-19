package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import StateCode.StateCode;

/**
 * Use to get out of some wrong ip address after trying for some times.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 10:32:29 PM
 */
public class ExecuteThread extends Thread {
	private int connectState;
	private Socket socket;
	private String address;
	private int port;
	
	private JSONObject reqJSON;
	private JSONObject resJSON;
	
	private JSONObject parseResString(String res) {
		JSONObject resJSON = null;
		try {
			JSONParser parser = new JSONParser();
			resJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resJSON;
	}
	
	public int getConnectState() {
		return connectState;
	}
	
	public JSONObject getResJSON() {
		if (connectState != StateCode.CONNECTION_SUCCESS)
			resJSON.put("state", connectState);
		return resJSON;
	}
	
	public ExecuteThread(String address, int port, JSONObject reqJSON) {
		this.address = address;
		this.port = port;
		this.connectState = StateCode.CONNECTION_FAIL;
		this.reqJSON = reqJSON;
		this.resJSON = new JSONObject();
		socket = null;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);  // has problem				
			DataInputStream reader = new DataInputStream(socket.getInputStream());
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			writer.writeUTF(reqJSON.toJSONString());
			writer.flush();
			String res = reader.readUTF();
			resJSON = parseResString(res);
			reader.close();
			writer.close();	
			connectState = StateCode.CONNECTION_SUCCESS;
			System.out.println("Connect to server successfully.");
		} catch (UnknownHostException e) {
			connectState = StateCode.UNKNOWN_HOST;
			System.out.println("Error: UNKNOWN HOST!");
		} catch (ConnectException e) {
			connectState = StateCode.COLLECTIONG_REFUSED;
			System.out.println("Error: COLLECTIONG REFUSED!");
		} catch (SocketTimeoutException e) {
			connectState = StateCode.TIMEOUT;
			System.out.println("Timeoutr!");
		} catch (SocketException e) {
			connectState = StateCode.IO_ERROR;
			System.out.println("Error: I/O ERROR!");
		} catch (IOException e) {
			connectState = StateCode.IO_ERROR;
			System.out.println("Error: I/O ERROR!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

