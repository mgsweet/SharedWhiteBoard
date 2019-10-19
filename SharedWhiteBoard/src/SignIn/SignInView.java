package SignIn;

import java.util.regex.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPanel;

import StateCode.StateCode;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import Client.Client;

/**
 * SignIn View
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 11:22:08 AM
 */

public class SignInView {
	private JFrame frame;
	protected JTextField userIdTextField;
	protected JTextField addressTextField;
	protected JTextField portTextField;
	protected JLabel lblIdWarn = null;
	protected JLabel lblAddressWarn = null;
	protected JLabel lblPortWarn = null;
	
	protected Client client = null;
	
	private SignInControler controler = null;
	private JLabel tipsLabel;
	
	/**
	 * Create the application.
	 */
	public SignInView(Client client) {
		this.client = client;
		initialize();
		this.controler = new SignInControler(this);
	}
	
	
	public JFrame getFrame() {
		return frame;
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
				if (controler.validateFormat()) {
					tipsLabel.setText("Loading...");
					tipsLabel.setVisible(true);
					// I discoever that the function above is asynchronous. Use the invokeLater to keep it work synchronous.
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								client.setUserId(controler.userId);
								client.setServerIp(controler.address);
								client.setPort(controler.port);
								int state = client.register();
								if (state == StateCode.SUCCESS) {
									client.switchToLobby();
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
		addressTextField.setText(client.getUserIp());
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
