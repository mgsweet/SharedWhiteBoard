package Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.SwingUtilities;

public class ChatClient implements Runnable {
	ChatPanel chatPanel;
	String userId;

	Socket socket;
	Thread thread;
	DataInputStream in;
	DataOutputStream out;
	boolean bConnected;

	int chatPort;
	String ip;

	public ChatClient(String userId, String ip, int chatPort) {
		this.userId = userId;
		this.ip = ip;
		this.chatPort = chatPort;
		try {
			init();
			startConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ChatPanel getPanel() {
		return chatPanel;
	}

	private void init() throws Exception {
		chatPanel = new ChatPanel();
		chatPanel.btnSend.addActionListener(e -> {
			if (chatPanel.txtInput.getText().length() != 0) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
					sendMsg('[' + df.format(new Date()) + "] " + userId + ":\n" + this.chatPanel.txtInput.getText());
				} catch (IOException e2) {
					processMsg(e2.toString());
				}
			}

		});
	}

	public void startConnect() {
		bConnected = false;
		try {
			socket = new Socket(ip, chatPort);
			bConnected = true;
			processMsg("Connection ok");
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			processMsg("Connection failed");
		}
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void endConnect() {
		try {
			in.close();
			out.close();
			socket.close();
			thread.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// run()方法作用：一直和服务端通讯，包括接收信息receiveMsg()、处理信息processMsg()
	public void run() {
		while (true) {
			try {
				String msg = receiveMsg();
				Thread.sleep(100L); //
				if (msg != "") {
					processMsg(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException ei) {
			}
		}
	}

	// 发送信息，out流写出去，写到服务端
	public void sendMsg(String msg) throws IOException {
		sendEncrypted(msg, out);
		out.flush();
	}

	// 接收信息，in流
	public String receiveMsg() throws IOException {
		try {
			if (in.available() > 0) {
				String msg = in.readUTF();
				return msg;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 处理信息（显示信息）
	public void processMsg(String str) {
		SwingUtilities.invokeLater(() -> {
			chatPanel.textArea.append(str + '\n');
		});
	}

	private static void sendEncrypted(String message, DataOutputStream out) {
		// Encrypt first
		String key = "5v8y/B?D(G+KbPeS";
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			// Perform encryption
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
			System.err.println("Encrypted text: " + new String(encrypted));
			out.writeUTF(Base64.getEncoder().encodeToString(encrypted));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
