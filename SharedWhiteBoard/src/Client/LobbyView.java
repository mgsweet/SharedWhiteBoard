/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import StateCode.StateCode;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LobbyView {

	protected JFrame frame;
	private JTextField roomNameTextField;
	private JTextField hostNameTextField;
	
	private JPanel roomsListPanel = null;
	private JButton btnCreateRoom = null;
	private JPanel firstPanel = null;
	private JPanel blankPanel = null;
	private JScrollPane scrollPane = null;
	private Vector<JButton> roomsBtnVec;
	private Client controler = null;
	
	private String addImagePath = "images/add.png";
	private String joinImagePath = "images/join.png";

	/**
	 * Create the application.
	 */
	public LobbyView(Client contorler) {
		this.controler = contorler;
		roomsBtnVec = new Vector<JButton>();
		initialize();
		refreshRoomsList();
	}
	
	private void reFreshRoomsListPanel() {
		roomsListPanel.removeAll();
		roomsListPanel.setPreferredSize(new Dimension(0, 170));
		firstPanel.removeAll();
		roomsListPanel.revalidate();
		roomsListPanel.repaint();
		roomsListPanel.add(firstPanel);
		firstPanel.add(btnCreateRoom);
	}
	
	private void filtRoomsList() {
		reFreshRoomsListPanel();
		
		JPanel currentPanel = firstPanel;
		int i = 0;
		for (JButton btn : roomsBtnVec) {
			if (i % 2 != 0) {
				roomsListPanel.setPreferredSize(new Dimension(0, (i/2 + 2) * 170));
				JPanel temp = new JPanel();
				temp.setBounds(5, (i/2 + 1) * 170, 570, 160);
				temp.setLayout(new GridLayout(1, 2, 5, 0));
				currentPanel = temp;
				roomsListPanel.add(temp);
			} 
			String[] roomInfo = btn.getText().split(" - ");
			if ((roomNameTextField.getText().equals(roomInfo[0]) || roomNameTextField.getText().equals("")) 
					&& (hostNameTextField.getText().equals(roomInfo[1]) || hostNameTextField.getText().equals(""))) {
				currentPanel.add(btn);
				i++;
			}
		}
		if (i % 2 == 0) {
		    currentPanel.add(blankPanel);
		}
	}
	
	private JSONObject parseResString(String res) {
		JSONObject resJSON = null;
		try {
			JSONParser parser = new JSONParser();
			resJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resJSON;
	}
	
	protected void refreshRoomsList() {
		// sent request to central server to gain roomlists
		try {
			System.out.println("Request for rooms list...");
			Socket client = new Socket(controler.address, controler.port);
			DataInputStream reader = new DataInputStream(client.getInputStream());
			DataOutputStream writer = new DataOutputStream(client.getOutputStream());
			JSONObject reqJson = new JSONObject();
			reqJson.put("command", StateCode.GET_ROOMS_LIST);
			writer.writeUTF(reqJson.toJSONString());
			writer.flush();
			String res = reader.readUTF();
			JSONObject resJson = parseResString(res);
			controler.roomsList = (Map<Integer, String>) resJson.get("roomsList");
			System.out.println("Get rooms list!");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		// repaint the roomslist panel
		roomsBtnVec.clear();
		reFreshRoomsListPanel();
		
		int i = 0;
		JPanel currentPanel = firstPanel;		
		for (Map.Entry<Integer, String> entry: controler.roomsList.entrySet()) {
			JButton tempBtn = new JButton();
			String[] roomInfo = entry.getValue().split(" ");
			String roomName = roomInfo[0];
			String hostName = roomInfo[1];
			tempBtn.setText(roomName + " - " + hostName);
			ImageIcon joinIcon = new ImageIcon(joinImagePath);
			joinIcon.setImage(joinIcon.getImage().getScaledInstance(50, 50,
					Image.SCALE_DEFAULT));
			tempBtn.setIcon(joinIcon);
			tempBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showInputDialog(frame, "Please Enter Password:", roomName, JOptionPane.OK_CANCEL_OPTION);
					//TODO
				}
			});
			roomsBtnVec.add(tempBtn);
			
			if (i % 2 != 0) {
				roomsListPanel.setPreferredSize(new Dimension(0, (i/2 + 2) * 170));
				JPanel temp = new JPanel();
				temp.setBounds(5, (i/2 + 1) * 170, 570, 160);
				temp.setLayout(new GridLayout(1, 2, 5, 0));
				currentPanel = temp;
				roomsListPanel.add(temp);
			} 
			
			currentPanel.add(tempBtn);
			i++;
		}
		if (i % 2 == 0) {
		    currentPanel.add(blankPanel);
		}
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
				System.out.println("Create Room");
				RoomCreateDialog.showCreateRoomDialog(frame, frame);
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
				refreshRoomsList();
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
				filtRoomsList();
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
