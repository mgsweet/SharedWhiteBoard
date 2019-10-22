package Chat;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;


public class ChatPanel extends JPanel {
	private static final long serialVersionUID = 4162325845732304245L;
	JTextField txtInput;
	JButton btnSend;
	JList<String> lstMsg = new JList<>();
	DefaultListModel<String> lstMsgModel = new DefaultListModel<>();
	
	public ChatPanel() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		setLayout(new BorderLayout(0, 0));
		JScrollPane chatScrollPane = new JScrollPane(lstMsg);
		add(chatScrollPane, BorderLayout.CENTER);
		JPanel pnlFoot = new JPanel();
		pnlFoot.setLayout(new BorderLayout(0, 0));
		txtInput = new JTextField("", 10);
		txtInput.setToolTipText("Please input.");
		pnlFoot.add(txtInput, BorderLayout.CENTER);
		btnSend = new JButton("Send");
		btnSend.setPreferredSize(new Dimension(60, 0));
		pnlFoot.add(btnSend, BorderLayout.EAST);
		add(pnlFoot, BorderLayout.SOUTH);

		lstMsg.setModel(lstMsgModel);
	}
	
}
	

