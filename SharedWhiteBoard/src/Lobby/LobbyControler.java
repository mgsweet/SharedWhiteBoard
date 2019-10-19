package Lobby;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Client.Client;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 12:03:10 PM
 */

public class LobbyControler {
	private Client client;
	private LobbyView ui;
	
	public LobbyControler(Client client, LobbyView ui) {
		this.client = client;
		this.ui = ui;
	}
	
	/**
	 * Get rooms list from central server.
	 */
	public void refreshRoomsList() {
		// sent request to central server to gain roomlist.
		client.pullRemoteRoomList();
		
		// repaint the roomlist panel.
		ui.roomsBtnVec.clear();
		reFreshRoomsListPanel();
		
		int i = 0;
		JPanel currentPanel = ui.firstPanel;		
		for (Map.Entry<Integer, String> entry: client.roomList.entrySet()) {
			JButton tempBtn = new JButton();
			String[] roomInfo = entry.getValue().split(" ");
			String roomName = roomInfo[0];
			String hostName = roomInfo[1];
			tempBtn.setText(roomName + " - " + hostName);
			ImageIcon joinIcon = new ImageIcon(ui.joinImagePath);
			joinIcon.setImage(joinIcon.getImage().getScaledInstance(50, 50,
					Image.SCALE_DEFAULT));
			tempBtn.setIcon(joinIcon);
			tempBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showInputDialog(ui.frame, "Please Enter Password:", roomName, JOptionPane.OK_CANCEL_OPTION);
					//TODO
				}
			});
			ui.roomsBtnVec.add(tempBtn);
			
			if (i % 2 != 0) {
				ui.roomsListPanel.setPreferredSize(new Dimension(0, (i/2 + 2) * 170));
				JPanel temp = new JPanel();
				temp.setBounds(5, (i/2 + 1) * 170, 570, 160);
				temp.setLayout(new GridLayout(1, 2, 5, 0));
				currentPanel = temp;
				ui.roomsListPanel.add(temp);
			} 
			
			currentPanel.add(tempBtn);
			i++;
		}
		if (i % 2 == 0) {
		    currentPanel.add(ui.blankPanel);
		}
	}
	
	protected void reFreshRoomsListPanel() {
		ui.roomsListPanel.removeAll();
		ui.roomsListPanel.setPreferredSize(new Dimension(0, 170));
		ui.firstPanel.removeAll();
		ui.roomsListPanel.revalidate();
		ui.roomsListPanel.repaint();
		ui.roomsListPanel.add(ui.firstPanel);
		ui.firstPanel.add(ui.btnCreateRoom);
	}
	
	protected void filtRoomsList() {
		reFreshRoomsListPanel();
		
		JPanel currentPanel = ui.firstPanel;
		int i = 0;
		for (JButton btn : ui.roomsBtnVec) {
			if (i % 2 != 0) {
				ui.roomsListPanel.setPreferredSize(new Dimension(0, (i/2 + 2) * 170));
				JPanel temp = new JPanel();
				temp.setBounds(5, (i/2 + 1) * 170, 570, 160);
				temp.setLayout(new GridLayout(1, 2, 5, 0));
				currentPanel = temp;
				ui.roomsListPanel.add(temp);
			} 
			String[] roomInfo = btn.getText().split(" - ");
			if ((ui.roomNameTextField.getText().equals(roomInfo[0]) || ui.roomNameTextField.getText().equals("")) 
					&& (ui.hostNameTextField.getText().equals(roomInfo[1]) || ui.hostNameTextField.getText().equals(""))) {
				currentPanel.add(btn);
				i++;
			}
		}
		if (i % 2 == 0) {
		    currentPanel.add(ui.blankPanel);
		}
	}
	

}
