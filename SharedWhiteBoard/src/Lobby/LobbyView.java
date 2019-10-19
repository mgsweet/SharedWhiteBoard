package Lobby;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

import Client.Client;
import util.WindowCloseListener;

/**
 * 
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 2:53:21 PM
 */

public class LobbyView {

	protected JFrame frame;
	protected JTextField roomNameTextField;
	protected JTextField hostNameTextField;
	
	protected JPanel roomsListPanel;
	protected JButton btnCreateRoom;
	protected JPanel firstPanel;
	protected JPanel blankPanel;
	protected JScrollPane scrollPane;
	protected Vector<JButton> roomsBtnVec;
	protected Client client;
	protected LobbyControler controler;
	
	protected String addImagePath = "images/add.png";
	protected String joinImagePath = "images/join.png";

	/**
	 * Create the application.
	 */
	public LobbyView(Client client) {
		this.client = client;
		roomsBtnVec = new Vector<JButton>();
		initialize();
		controler = new LobbyControler(client, this);
		controler.refreshRoomsList();
	}
	
	/**
	 * Get current frame.
	 * @return
	 */
	public JFrame getFrame() {
		return this.frame;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("SharedWhiteBoard - Lobby");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowCloseListener(client));
		frame.setMinimumSize(new Dimension(600, 500));
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		roomsListPanel = new JPanel();
		//panel.setPreferredSize(new Dimension(0, 500));
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(roomsListPanel);
		roomsListPanel.setLayout(null);
		
		// FirstPanel In Roomslist Panel INIT
		firstPanel = new JPanel();
		firstPanel.setBounds(5, 5, 570, 160);
		firstPanel.setLayout(new GridLayout(1, 2, 5, 0));
		
		// CREATE_ROOM button INIT
		btnCreateRoom = new JButton();
		btnCreateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RoomCreateDialog.showCreateRoomDialog(frame, frame, client);
			}
		});
		ImageIcon addIcon = new ImageIcon(addImagePath);
		addIcon.setImage(addIcon.getImage().getScaledInstance(50, 50,
				Image.SCALE_DEFAULT));
		btnCreateRoom.setIcon(addIcon);
		
		// init Blank Panel
		blankPanel = new JPanel();	
	
		JPanel controlBarPanel = new JPanel();
		controlBarPanel.setPreferredSize(new Dimension(0, 100));
		frame.getContentPane().add(controlBarPanel, BorderLayout.SOUTH);
		controlBarPanel.setLayout(new BorderLayout(0, 0));
		
		JButton btnRefresh = new JButton("REFRESH");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controler.refreshRoomsList();
			}
		});
		controlBarPanel.add(btnRefresh, BorderLayout.EAST);
		
		// filterPanel
		JPanel filterPanel = new JPanel();
		controlBarPanel.add(filterPanel, BorderLayout.CENTER);
		filterPanel.setLayout(new BorderLayout(0, 0));
		
		JButton btnFilt = new JButton("FILT");
		btnFilt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controler.filtRoomsList();
			}
		});
		
		filterPanel.add(btnFilt, BorderLayout.EAST);
		JPanel filterTextPanel = new JPanel();
		filterPanel.add(filterTextPanel, BorderLayout.CENTER);
		JLabel lblRoomName = new JLabel("Room Name:");
		JLabel lblHostName = new JLabel("Host Name:");
		
		roomNameTextField = new JTextField();
		roomNameTextField.setColumns(10);
		
		hostNameTextField = new JTextField();
		hostNameTextField.setColumns(10);
		GroupLayout gl_filterTextPanel = new GroupLayout(filterTextPanel);
		gl_filterTextPanel.setHorizontalGroup(
			gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterTextPanel.createSequentialGroup()
					.addGap(50)
					.addGroup(gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_filterTextPanel.createSequentialGroup()
							.addComponent(lblRoomName, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(roomNameTextField))
						.addGroup(gl_filterTextPanel.createSequentialGroup()
							.addGap(7)
							.addComponent(lblHostName, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(hostNameTextField)))
					.addGap(50))
		);
		gl_filterTextPanel.setVerticalGroup(
			gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterTextPanel.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_filterTextPanel.createSequentialGroup()
							.addGap(5)
							.addComponent(lblRoomName))
						.addComponent(roomNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(2)
					.addGroup(gl_filterTextPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(hostNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHostName))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		filterTextPanel.setLayout(gl_filterTextPanel);
	}
}
