/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import java.awt.EventQueue;

import java.util.regex.*;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignInView {

	protected JFrame frame;
	private JTextField userIdTextField;
	private JTextField addressTextField;
	private JTextField portTextField;
	private JLabel lblIdWarn = null;
	private JLabel lblAddressWarn = null;
	private JLabel lblPortWarn = null;
	private Client controler = null;
	
	private String userId = "";
	private String address = "";
	private int port = -1;
	
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
	 * When you need to debug this view:
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SignInView window = new SignInView();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public SignInView(Client client) {
		this.controler = client;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel titleJLabel = new JLabel("Shared White Board 1.0");
		titleJLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		
		JPanel panel = new JPanel();
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateFormat()) {
					controler.userId = userId;
					controler.address = address;
					controler.port = port;
					controler.signInView.frame.setVisible(false);
					controler.lobbyView.frame.setVisible(true);
					//TODO
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(105)
					.addComponent(titleJLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(105))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(155)
					.addComponent(btnJoin, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
					.addGap(154))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(titleJLabel)
					.addGap(18)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 127, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(btnJoin, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
					.addGap(32))
		);
		
		JLabel lblUserId = new JLabel("User ID:");
		
		JLabel lblServerAddress = new JLabel("Server Address:");
		
		JLabel lblPort = new JLabel("Port:");
		
		userIdTextField = new JTextField();
		userIdTextField.setColumns(10);
		
		addressTextField = new JTextField();
		addressTextField.setColumns(10);
		
		portTextField = new JTextField();
		portTextField.setColumns(10);
		
		lblIdWarn = new JLabel("\"\\w{1,8}\"");
		lblIdWarn.setForeground(Color.RED);
		lblIdWarn.setVisible(false);
		
		lblAddressWarn = new JLabel("\"Invalid Port!\"");
		lblAddressWarn.setForeground(Color.RED);
		lblAddressWarn.setVisible(false);
		
		lblPortWarn = new JLabel("\"Format Wrong\"");
		lblPortWarn.setForeground(Color.RED);
		lblPortWarn.setVisible(false);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(82)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblServerAddress)
						.addComponent(lblUserId)
						.addComponent(lblPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(userIdTextField)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(portTextField, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPortWarn))
						.addComponent(addressTextField))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblIdWarn)
						.addComponent(lblAddressWarn))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(33)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserId)
						.addComponent(userIdTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblIdWarn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerAddress)
						.addComponent(addressTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAddressWarn))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(portTextField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPortWarn))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
}
