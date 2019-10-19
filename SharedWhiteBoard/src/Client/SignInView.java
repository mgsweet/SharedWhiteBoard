/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import java.util.regex.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import CentralServer.CentralServer;
import StateCode.StateCode;
import TextFieldFilter.NumberTextField;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SignInView {

	protected JFrame frame;
	private JTextField userIdTextField;
	private JTextField addressTextField;
	private JTextField portTextField;
	private JLabel lblIdWarn = null;
	private JLabel lblAddressWarn = null;
	private JLabel lblPortWarn = null;
	private Client controler = null;
	
	private JLabel tipsLabel;
	
	private String userId = "";
	private String address = "";
	private int port = -1;
	
	/**
	 * Create the application.
	 */
	public SignInView(Client client) {
		this.controler = client;
		initialize();
	}
	
//	public void setTextOfTips(String tips) {
//		tipsLabel.setText(tips);
//	}
	
	private Boolean validateFormat() {
		Boolean checkId = false;
		Boolean checkAddress = false;
		Boolean checkPort = false;
		String idPatten = "^\\w{1,8}$";
		String portPatten = "^\\d+$";
		String addressPatten = "^.{1,20}$";
		// check userId
		userId = userIdTextField.getText();
		if (!Pattern.matches(idPatten, userId)) {
			lblIdWarn.setVisible(true);
			checkId = false;
		} else {
			lblIdWarn.setVisible(false);
			checkId = true;
		}
		
		// check address
		address = addressTextField.getText();
		if (!Pattern.matches(addressPatten, address)) {
			lblAddressWarn.setVisible(true);
			checkAddress = false;
		} else {
			lblAddressWarn.setVisible(false);
			checkAddress = true;
		}
		
		//check port
		String portStr = portTextField.getText();
		if (!Pattern.matches(idPatten, portStr)) {
			lblPortWarn.setVisible(true);
			checkPort = false;
		} else {
			try {
				port = Integer.parseInt(portStr);
				if (port <= 1024 || port >= 49151) {
					lblPortWarn.setVisible(true);
					checkPort = false;
				} else {
					lblPortWarn.setVisible(false);
					checkPort = true;
				}
			} catch (Exception e) {
				lblPortWarn.setVisible(true);
				checkPort = false;
			}
		}
		return checkId && checkPort && checkAddress;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Sign In Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel titleJLabel = new JLabel("Shared White Board 1.0");
		titleJLabel.setBounds(105, 6, 246, 25);
		titleJLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(0, 30, 450, 127);
		
		tipsLabel = new JLabel("Loading...");
		tipsLabel.setVisible(false);
		tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tipsLabel.setBounds(108, 173, 234, 16);
		frame.getContentPane().add(tipsLabel);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.setBounds(155, 201, 147, 45);
		// Join logic here.
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateFormat()) {
					tipsLabel.setText("Loading...");
					tipsLabel.setVisible(true);
					// I discoever that the function above is asynchronous. Use the invokeLater to keep it work synchronous.
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								controler.userId = userId;
								controler.serverIp = address;
								controler.port = port;
								int state = controler.register();
								if (state == StateCode.SUCCESS) {
									controler.switchToLobby();
								} else if (state == StateCode.FAIL){
									tipsLabel.setText("User name exist! Change one!");
									tipsLabel.setVisible(true);
								} else {
									tipsLabel.setText("Can not connect to the server!");
									tipsLabel.setVisible(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		
		JLabel lblUserId = new JLabel("User ID:");
		lblUserId.setBounds(129, 38, 50, 16);
		
		JLabel lblServerAddress = new JLabel("Server Address:");
		lblServerAddress.setBounds(82, 70, 97, 16);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(150, 102, 29, 16);
		
		userIdTextField = new JTextField();
		userIdTextField.setBounds(185, 33, 130, 26);
		userIdTextField.setDocument(new TextFieldFilter.NumberTextField(8, false));
		userIdTextField.setToolTipText("Please input a name.");
		userIdTextField.setColumns(8);
		
		addressTextField = new JTextField();
		addressTextField.setBounds(185, 65, 130, 26);
		addressTextField.setText(controler.getUserIp());
		addressTextField.setToolTipText("Please input a ip address.");
		addressTextField.setColumns(10);
		
		portTextField = new JTextField();
		portTextField.setText("4443");
		portTextField.setBounds(185, 97, 58, 26);
		portTextField.setToolTipText("Please input a port number.");
		portTextField.setColumns(10);
		
		lblIdWarn = new JLabel("A-Za-z0-9_");
		lblIdWarn.setBounds(321, 38, 78, 16);
		lblIdWarn.setForeground(Color.RED);
		lblIdWarn.setVisible(false);
		
		lblAddressWarn = new JLabel("Invalid!");
		lblAddressWarn.setBounds(321, 70, 46, 16);
		lblAddressWarn.setForeground(Color.RED);
		lblAddressWarn.setVisible(false);
		
		lblPortWarn = new JLabel("Invalid!");
		lblPortWarn.setBounds(249, 102, 46, 16);
		lblPortWarn.setForeground(Color.RED);
		lblPortWarn.setVisible(false);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(titleJLabel);
		frame.getContentPane().add(btnJoin);
		frame.getContentPane().add(infoPanel);
		infoPanel.setLayout(null);
		infoPanel.add(lblServerAddress);
		infoPanel.add(lblUserId);
		infoPanel.add(lblPort);
		infoPanel.add(userIdTextField);
		infoPanel.add(portTextField);
		infoPanel.add(lblPortWarn);
		infoPanel.add(addressTextField);
		infoPanel.add(lblIdWarn);
		infoPanel.add(lblAddressWarn);
	}
}
