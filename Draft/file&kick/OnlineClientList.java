import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Unfinished
public class OnlineClientList extends JScrollPane {
	JList clientList;
    public OnlineClientList() {
    	//Get online-user list
    	String[] clients = { "client1", "client2", "client3", "client 4", "client5", "client6" };
    	
        //JFrame f = new JFrame();
        //f.setSize(600, 500);
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JPanel panel = new JPanel();
        setPreferredSize(new Dimension(200,100));
        DefaultListModel model = new DefaultListModel(); 
        int index = 0;
        while (index < clients.length) {
        	model.addElement(clients[index++]);
		}
        clientList = new JList(model);
        
        clientList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!clientList.getValueIsAdjusting()){    
                    System.out.println(clientList.getSelectedValue());
                }
            }
        });

        setViewportView(clientList);
        //panel.add(scrollPane);
        //f.getContentPane().add(panel);
        //f.setVisible(true);
        
    }
}
