package Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//每一个连接都是一个线程
//This class is the thread that handles all communication with a client
class Connection extends Thread {
	protected Socket client;
	protected DataInputStream in;
	protected DataOutputStream out;
	ChatServer server;

	// Initialize the stream and start the thread
	public Connection(Socket client_socket, ChatServer server_frame) {
		client = client_socket;
		server = server_frame;
		try {
			in = new DataInputStream(client.getInputStream());
		    out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
			return;
		}
		this.start();
	}
	
	public void disconnect() {
		try {
			out.close();
			in.close();
			client.close();
			interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Connection里面这个线程也是无限循环的，用来接收信息、处理信息
	// Provide service.
	// Read a line
	public void run() {
		try {
			for (;;) {
				String line = receiveMsg();
				if (Thread.currentThread().isInterrupted()) break;
				server.processMsg(line);
				if (line == null)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void sendMsg(String msg) throws IOException {
		out.writeUTF(msg);
		out.flush();
	}

	public String receiveMsg() throws IOException {
		try {
			String msg = in.readUTF();
			msg = decryptMessage(msg);
			return msg;
		} catch (IOException e) {
			disconnect();
			//e.printStackTrace();
		}
		return "";
	}
	
	
	private static String decryptMessage(String message){
		// Decrypt result
		try {
 		String key = "5v8y/B?D(G+KbPeS";
 		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			message = new String(cipher.doFinal(Base64.getDecoder().decode(message.getBytes())));
			System.err.println("Decrypted message: "+message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}
	
	
}
