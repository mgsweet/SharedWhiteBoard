package ch11;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
public class ChatServer extends JFrame implements Runnable {
    JTextField txtInput = new JTextField("please input here", 20);
    JButton btnSend = new JButton("Send");
    JList<String> lstMsg = new JList<>();
    DefaultListModel<String> lstMsgModel = new DefaultListModel<>();

    public ChatServer() {
        try {
            init();
			ServerListen();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception  {
        getContentPane().add(
			new JScrollPane(lstMsg), BorderLayout.CENTER);
		JPanel pnlFoot = new JPanel();
		pnlFoot.add(txtInput);
		pnlFoot.add(btnSend);
        getContentPane().add(pnlFoot, BorderLayout.SOUTH);

        lstMsg.setModel(lstMsgModel);
		btnSend.addActionListener((e)-> {
			processMsg(this.txtInput.getText());
        });

        this.setSize(400, 300);
        this.setTitle("Chat Server");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            new ChatServer();
        });
    }

    //处理信息：1.把信息显示到列表框里。2.广播信息
    public void processMsg( String str ) {
        SwingUtilities.invokeLater( ()-> {
            lstMsgModel.addElement(str);
        });

        broadcastMsg(str);
    }

    //广播：用一个循环把信息发给所有连接
    public void broadcastMsg( String str ) {
        try {
            for(Connection client : clients) {
                client.sendMsg(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void btnSend_actionPerformed(ActionEvent e) {
        broadcastMsg( this.txtInput.getText() );
    }

    public final static int DEFAULT_PORT = 6543;
    protected ServerSocket listen_socket;
    Thread thread;
    java.util.Vector<Connection> clients = new java.util.Vector<>();//Vector是clients的集合，是线程安全的！

    //Create a ServerSocket to listen for connections on; start the thread 
    public void ServerListen() {
        try {
            listen_socket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        processMsg("Server: listening on port " + DEFAULT_PORT);
        //这个线程是server thread，处理多个客户，下面的run()函数就是这个线程的
        thread = new Thread(this);
        thread.start();
    }
    
    //The body of the server thread.(上面的thread) 
    //Loop forever,listening for and accepting connections from clients. 
    //For each connection, create a Connection object to handle communication through the new Socket.
    public void run() {
        try {
            while(true) {
                Socket client_socket = listen_socket.accept();
                //封装成功能更强的Connection对象
                Connection c = new Connection(client_socket, this);
                clients.add( c );
                processMsg( "One Client Comes in");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//每一个连接都是一个线程
//This class is the thread that handles all communication with a client
class Connection extends Thread {
    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;
    ChatServer server;

    //Initialize the stream and start the thread 
    public Connection(Socket client_socket, ChatServer server_frame) {
        client = client_socket;
        server = server_frame;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new java.io.PrintWriter(client.getOutputStream());
        }
        catch (IOException e) {
            try {
                client.close();
            }
            catch (IOException e2) {
                ;
            }
            e.printStackTrace();
            return;
        }
        this.start();
    }

    //Connection里面这个线程也是无限循环的，用来接收信息、处理信息
    //Provide service.
    //Read a line
    public void run() {
        try {
            for(;;) {
                String line = receiveMsg();
                server.processMsg( line );
                if (line == null) break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                client.close();
            }
            catch (IOException e2) {
                ;
            }
        }
    }

    public void sendMsg(String msg) throws IOException {
        out.println( msg );
        out.flush();
    }

    public String receiveMsg()  throws IOException {
        try {
            String msg = in.readLine();
            return msg;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

