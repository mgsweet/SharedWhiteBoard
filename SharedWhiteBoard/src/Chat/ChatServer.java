package Chat;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Base64;
import java.util.Iterator;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.SwingUtilities;

public class ChatServer implements Runnable{
	private ChatPanel chatPanel;
	public final static int DEFAULT_PORT = 6543;
	protected ServerSocket listen_socket;
	private Thread thread;
	private Vector<Connection> clients;
	
	public ChatServer() {
		try {
			init();
			ServerListen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ChatPanel getPanel() {
		return chatPanel;
	}
	
	private void init() throws Exception {
		chatPanel = new ChatPanel();
		clients = new Vector<>();// Vector是clients的集合，是线程安全的！
		chatPanel.btnSend.addActionListener((e) -> {
			processMsg(this.chatPanel.txtInput.getText());
		});
	}

	// 处理信息：1.把信息显示到列表框里。2.广播信息
	public void processMsg(String str) {
		SwingUtilities.invokeLater(() -> {
			chatPanel.lstMsgModel.addElement(str);
		});

		broadcastMsg(str);
	}

	// 广播：用一个循环把信息发给所有连接
	public void broadcastMsg(String str) {
		Iterator<Connection> iter = clients.iterator();
		Connection client;
		while (iter.hasNext()) {
			client = iter.next();
			try {
				client.sendMsg(str);
			} catch (Exception e) {
				client.disconnect();
				iter.remove();
				e.printStackTrace();
			}
			
		}
	}

	void btnSend_actionPerformed(ActionEvent e) {
		broadcastMsg(chatPanel.txtInput.getText());
	}

	// Create a ServerSocket to listen for connections on; start the thread
	public void ServerListen() {
		try {
			listen_socket = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		processMsg("Server: listening on port " + DEFAULT_PORT);
		// 这个线程是server thread，处理多个客户，下面的run()函数就是这个线程的
		thread = new Thread(this);
		thread.start();
	}

	// The body of the server thread.(上面的thread)
	// Loop forever,listening for and accepting connections from clients.
	// For each connection, create a Connection object to handle communication
	// through the new Socket.
	public void run() {
		try {
			while (true) {
				Socket client_socket = listen_socket.accept();
				// 封装成功能更强的Connection对象
				Connection c = new Connection(client_socket, this);
				clients.add(c);
				processMsg("One Client Comes in");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


