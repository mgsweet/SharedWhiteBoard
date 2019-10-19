package WhiteBoard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClientListScrollPanel extends JPanel {
	private JScrollPane scrollpane;
	private JList<String> clientList;
	private DefaultListModel<String> model;
	private Vector<String> clients;
	private Vector<String> newClients;
	private JButton btn1;
	private JButton btn2;
	private List<String> selectClients;

	public ClientListScrollPanel() {		
    	//Get client-list
		clients = new Vector<String>();
		clients = getClientList();
		
		//initialize
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		scrollpane = new JScrollPane();
		scrollpane.setPreferredSize(new Dimension(200,200));
        model = new DefaultListModel<String>();
        for (int index = 0; index < clients.size();index++) {
        	
        	model.addElement(clients.get(index));
        	
		}
        
        clientList = new JList(model);
        clientList.setForeground(Color.black);
        //set multiple_selection mode. Press 'Ctrl' and select options
        clientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //select client(s)
        clientList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(!clientList.getValueIsAdjusting()){    
                	
                    selectClients = clientList.getSelectedValuesList();
                    setbtn2("Kick Out");
                
                }
            }
        });
        
        scrollpane.add(clientList);
        scrollpane.setViewportView(clientList);
        panel1.add(scrollpane);
        add(panel1, BorderLayout.CENTER);

        JPanel panel2 = new JPanel(new GridLayout(2,1));
        btn1 = new JButton("Agree");
        btn1.setForeground(Color.gray);
        
        btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectClients.size() != 0) {
					
					for(Iterator<String> it = selectClients.iterator(); it.hasNext();) {
						String client;
						String row = it.next();
						String[] spl = row.split("!");
						if(spl.length>1) {
							client = spl[1];
						} else {
							client = spl[0];
						}
						
						if(newClients.size() != 0 && newClients.indexOf(client) != -1) {
							if (agreeJoining(client)) {
								clients.add(client);
								newClients.remove(client);
								model.removeElement(row);
								model.addElement(client);
								it.remove();
							}
						}
					}
					
					JOptionPane.showMessageDialog(scrollpane, "Successful!");
					
				} else {
					
					JOptionPane.showMessageDialog(scrollpane, "Please select at least one client");
					
				}
				if(newClients.size() == 0) {
					btn1.setForeground(Color.gray);
				}
			}
		});
        panel2.add(btn1);
        
        setbtn2("Kick Out");
        panel2.add(btn2);
        add(panel2, BorderLayout.SOUTH);
           
    }
	
	
	// Get the client list in RMI
	protected Vector<String> getClientList(){
		
		
		return null;
	}
	
	// Need a Listener for coming clients
	protected void newClientListener() {
		newClients = new Vector<String>();
		
		if(true) {	//if new client coming !!!!!!
			
			selectClients = null;
			// Add new client(s) list to the JList
			for(int i=0; i<newClients.size(); i++) {
				model.add(0, "<html><font color=red>!"+newClients.get(i)+"!(asks for joining)</font></html>");
			}
			
			setbtn2("Agree all");
		}
		
	}

	
	private void setbtn2(String text) {
		btn2 = new JButton(text);
		if(text=="Kick Out") {	// To reject client entering or Kick out
			btn2.setForeground(Color.black);
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(selectClients.size() != 0) {
						
						for(Iterator<String> it = selectClients.iterator(); it.hasNext();) {
							String client;
							String row = it.next();
							String[] spl = row.split("!");
							if(spl.length>1) {
								client = spl[1];
							} else {
								client = spl[0];
							}
							if(newClients.size() != 0 && newClients.indexOf(client) != -1) {
								if(rejectJoining(client)) {	// Reject client who wants to enter the room
									newClients.remove(client);
									model.removeElement(row);
								}
								
							} else {
								if(clients.size() != 0 && clients.indexOf(client) != -1) {
									if(kickOut(client)) {	// Kick out the client
										clients.remove(client);
										model.removeElement(client);
									}
									
								}
							}
						}
						JOptionPane.showMessageDialog(scrollpane, "Successful!");
						
					} else {
						
						JOptionPane.showMessageDialog(scrollpane, "Please select at least one client");
						
					}
					
					btn1.setForeground(Color.black);
					btn2.setForeground(Color.black);
				}
			});
		} else {
			// Agree all
			btn1.setForeground(Color.red);
			btn2.setForeground(Color.red);
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(Iterator<String> it = newClients.iterator(); it.hasNext();) {
						String client = it.next();
						if (agreeJoining(client)) {
							clients.add(client);
							model.removeElement("<html><font color=red>!"+client+"!(asks for joining)</font></html>");
							model.addElement(client);
							it.remove();							
						}
												
					}
					btn1.setForeground(Color.black);
					btn2.setForeground(Color.black);
				}
			});
		}
		
	}
	
	// Kick out client(s) in RMI
	private boolean kickOut(String clientname) {
		
		return true;
	}
	
	// Agree client to enter in RMI
	private boolean agreeJoining(String clientname) {
		
		return true;
	}
	
	// Kick out client(s) in RMI
	private boolean rejectJoining(String clientname) {
			
		return true;
	}
	
}
