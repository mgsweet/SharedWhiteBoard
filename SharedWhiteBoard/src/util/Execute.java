package util;

import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSONObject;
import StateCode.StateCode;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 9:56:39 PM
 */

public class Execute {
	
	/**
	 * Send request to central server, which can detect whether the server address
	 * is workable or not.
	 * 
	 * @param reqJSON
	 * @return resJSON
	 */
	public static final JSONObject execute(JSONObject reqJSON, String ip, int port) {
		JSONObject resJSON = new JSONObject();
		try {
			System.out.println("Trying to connect to server...");
			ExecuteThread eThread = new ExecuteThread(ip, port, reqJSON);
			eThread.start();
			eThread.join(1500);
			if (eThread.isAlive()) {
				eThread.interrupt();
				throw new TimeoutException();
			}
			resJSON = eThread.getResJSON();
		} catch (TimeoutException e) {
			resJSON.put("state", StateCode.TIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resJSON;
	}

}
