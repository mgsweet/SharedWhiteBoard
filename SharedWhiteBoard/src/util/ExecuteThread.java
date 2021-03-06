package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
			socket = new Socket();  // has problem		
			socket.connect(new InetSocketAddress(address, port), 2000);
			DataInputStream reader = new DataInputStream(socket.getInputStream());
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			writer.writeUTF(reqJSON.toJSONString());
			writer.flush();
			String res = reader.readUTF();
			resJSON = JSON.parseObject(res);
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
			System.out.println("Error: Timeout!");
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

