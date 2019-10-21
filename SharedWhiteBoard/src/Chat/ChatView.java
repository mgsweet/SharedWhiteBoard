package Chat;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;


public class ChatView extends JPanel {
	private static final long serialVersionUID = 4162325845732304245L;
	JTextField txtInput = new JTextField("please input here", 20);
	JButton btnSend = new JButton("Send");
	JList<String> lstMsg = new JList<>();
	DefaultListModel<String> lstMsgModel = new DefaultListModel<>();
	
	public ChatView() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		setLayout(new BorderLayout(0, 0));
		add(new JScrollPane(lstMsg), BorderLayout.CENTER);
		JPanel pnlFoot = new JPanel();
		pnlFoot.add(txtInput);
		pnlFoot.add(btnSend);
		add(pnlFoot, BorderLayout.SOUTH);

		lstMsg.setModel(lstMsgModel);
//		btnSend.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				lstMsgModel.addElement(txtInput.getText());
//			}
//		});
		this.setSize(400, 300);
	}
	
}
	

