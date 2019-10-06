package ch11;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.net.*;
import java.io.*;


//客户端建立连接后，建了两个流，再用一个线程无线地处理两个流
public class ChatClient extends JFrame  implements Runnable {
	
	JTextField txtInput = new JTextField("please input here", 20);
    JButton btnSend = new JButton("Send");
    JButton btnStart = new JButton("Start conect to server");
    JList<String> lstMsg = new JList<>();
    DefaultListModel<String> lstMsgModel = new DefaultListModel<>();

    Socket sock;
    Thread thread;
    BufferedReader in;
    PrintWriter out;
    public final static int DEFAULT_PORT = 6543;
    boolean bConnected;
	
	public ChatClient() {
        try {
            init();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	private void init() throws Exception  {
		JPanel pnlHead = new JPanel();
		pnlHead.add( btnStart );
        getContentPane().add(pnlHead, BorderLayout.NORTH);

        getContentPane().add(
			new JScrollPane(lstMsg), BorderLayout.CENTER);
		JPanel pnlFoot = new JPanel();
		pnlFoot.add(txtInput);
		pnlFoot.add(btnSend);
        getContentPane().add(pnlFoot, BorderLayout.SOUTH);

        lstMsg.setModel(lstMsgModel);

        btnSend.addActionListener(e-> {
            if( txtInput.getText().length() != 0 ) {
                try {
                    sendMsg( txtInput.getText() );
                } catch(IOException e2) {
                    processMsg(e2.toString());
                }
            }

        });

        btnStart.addActionListener(e-> {
            this.startConnect() ;
        });

        this.setSize(400, 300);
        this.setTitle("Chat Client");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new ChatClient();
		});
    }
	
    public void startConnect() {
        bConnected = false;
        try {
            sock = new Socket( "127.0.0.1", DEFAULT_PORT);
            bConnected = true;
            processMsg("Connection ok");
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new java.io.PrintWriter(sock.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
            processMsg("Connection failed");
        }
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    //run()方法作用：一直和服务端通讯，包括接收信息receiveMsg()、处理信息processMsg()
    public void run() {
        while(true) {
            try {
                String msg = receiveMsg();
                Thread.sleep(100L);  //
                if( msg != null ) {
                    processMsg( msg );
                }
            } catch( IOException e ) {
                e.printStackTrace();
            } catch( InterruptedException ei) {}
        }
    }

    //发送信息，out流写出去，写到服务端
    public  void sendMsg(String msg) throws IOException {
        out.println( msg );
        out.flush();
    }
    
    //接收信息，in流
    public  String receiveMsg()  throws IOException {
        try {
            String msg = in.readLine();
            return msg;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //处理信息（显示信息）
    public void processMsg( String str ) {
        SwingUtilities.invokeLater( ()-> {
            lstMsgModel.addElement(str);
        });
    }

    //匿名函数写法：
//    public void processMsg( String str ) {
//        SwingUtilities.invokeLater( new Runnable(){
//			@Override
//			public void run() {
//				lstMsgModel.addElement(str);
//			}
//        });
//    }
 
}