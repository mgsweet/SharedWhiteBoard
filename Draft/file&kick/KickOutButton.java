import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

//Unfinished
public class KickOutButton extends JButton{
	JPanel panel = null;
	OnlineClientList olistPane;
	
	public KickOutButton(JPanel panel, OnlineClientList olistPane)
	{
		super();
		this.panel = panel;
		this.olistPane = olistPane;
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {	
					if(olistPane != null) {
						String n = null;
						DefaultListModel model = (DefaultListModel) olistPane.clientList.getModel();
						String kickSomeone = (String) olistPane.clientList.getSelectedValue();
						int confirm = JOptionPane.showConfirmDialog(panel, "Kick out "+kickSomeone+"?", "Warning", JOptionPane.YES_NO_OPTION);
						if(confirm == JOptionPane.YES_OPTION) {
							kickout();
							model.removeElement(kickSomeone);
						}
					}
					
				} catch (Exception er) {
					JOptionPane.showMessageDialog(panel, "Failed to kick out");
					er.printStackTrace();
				}
			}
 
		});
		
	}
	
	//Delete from server side
	public void kickout(){
		
		
	}

}
