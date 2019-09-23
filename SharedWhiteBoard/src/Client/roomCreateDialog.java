package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

public class roomCreateDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField roomNameTextField;
	private JTextField PasswordTextField;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		try {
//			roomCreateDialog dialog = new roomCreateDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public roomCreateDialog() {
		setTitle("Create Room Dialog");
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblRoomName = new JLabel("Room Name:");
		lblRoomName.setBounds(99, 79, 106, 27);
		lblRoomName.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(99, 133, 106, 27);
		lblPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		
		roomNameTextField = new JTextField();
		roomNameTextField.setBounds(211, 79, 150, 27);
		roomNameTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		roomNameTextField.setColumns(10);
		
		PasswordTextField = new JTextField();
		PasswordTextField.setBounds(211, 133, 150, 27);
		PasswordTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		PasswordTextField.setColumns(10);
		contentPanel.setLayout(null);
		contentPanel.add(lblRoomName);
		contentPanel.add(roomNameTextField);
		contentPanel.add(lblPassword);
		contentPanel.add(PasswordTextField);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(0, 50));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
