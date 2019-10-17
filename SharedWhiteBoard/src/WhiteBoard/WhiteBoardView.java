package WhiteBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

/**
 * 
 * @author Aaron-Qiu
 *
 */

public class WhiteBoardView {

	private JFrame frame;
	private JColorChooser colorChooser;
	private DrawListener drawListener;
	private PaintBoardPanel paintBoardPanel;
	private JButton btnCurrentColor;
	// Color
	private Color currentColor;
	private Color backgroundColor = null;
	// Thickness
	private int thickness;
	// Title of the window
	private String title;

	// Paint history recorder
	private PaintManager paintManager;

	// Default color display in the left bottom.
	private static Color[] DEFAULTCOLORS = { Color.BLACK, Color.BLUE, Color.WHITE, Color.GRAY, Color.RED, Color.GREEN,
			Color.ORANGE, Color.YELLOW, Color.PINK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.CYAN, Color.MAGENTA,
			new Color(250, 128, 114), new Color(210, 105, 30), new Color(160, 32, 240) };

	// Use to create tool button.
	private static String[] TOOLNAME = { "pen", "line", "circle", "eraser", "rect", "oval", "roundrect", "text" };
	private JTextField thicknessTextField;

	/**
	 * Get frame window.
	 */
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Get getPaintBoardFrame window.
	 */
	public PaintBoardPanel getPaintBoardPanel() {
		return this.paintBoardPanel;
	}

	/**
	 * Get current select color.
	 */
	public Color getCurrentColor() {
		return currentColor;
	}

	/**
	 * Get thickness.
	 * 
	 * @return
	 */
	public int getThickness() {
		return thickness;
	}

	/**
	 * Get background color.
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Get paint history manager
	 */
	public PaintManager getPaintManager() {
		return paintManager;
	}

	/**
	 * Create the view without Paint Manager.
	 */
//	public WhiteBoardView() {
//		this.title = "Offline WhiteBoard";
//		currentColor = Color.BLACK;
//		backgroundColor = Color.WHITE;
//		colorChooser = new JColorChooser(currentColor);
//		this.paintManager = new PaintManager(PaintManager.OFFLINE_MODE);
//		initialize();
//	}

	/**
	 * Create the view with Paint Manager.
	 */
	public WhiteBoardView(PaintManager paintManager, String title) {
		this.title = title;
		currentColor = Color.BLACK;
		thickness = 2;
		backgroundColor = Color.WHITE;
		colorChooser = new JColorChooser(currentColor);
		this.paintManager = paintManager;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 700);
		frame.setTitle(title);
		frame.setResizable(true);

		// Add Action Listener
		drawListener = new DrawListener(this);

		// Add draw tool panel.
		JPanel drawToolPanel = new JPanel();
		drawToolPanel.setPreferredSize(new Dimension(110, 0));
		frame.getContentPane().add(drawToolPanel, BorderLayout.WEST);

		// Add Painting broad.
		paintBoardPanel = new PaintBoardPanel(paintManager);
		paintBoardPanel.setBackground(Color.white);
		paintBoardPanel.addMouseListener(drawListener);
		paintBoardPanel.addMouseMotionListener(drawListener);
		paintBoardPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		frame.getContentPane().add(paintBoardPanel, BorderLayout.CENTER);

		// Set the paint area in paintManager to the current paintBoard panel.
		paintManager.setPaintArea(paintBoardPanel);

		JPanel userPanel = new JPanel();
		userPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		userPanel.setPreferredSize(new Dimension(200, 0));
		userPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(userPanel, BorderLayout.EAST);
		userPanel.setLayout(new BorderLayout(0, 0));

		JPanel userControlPanel = new JPanel();
		userControlPanel.setPreferredSize(new Dimension(0, 300));
		userPanel.add(userControlPanel, BorderLayout.NORTH);
		userControlPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblUserList = new JLabel("User List:");
		userControlPanel.add(lblUserList, BorderLayout.NORTH);

		ClientListScrollPanel clientListScrollPanel = new ClientListScrollPanel();
		userControlPanel.add(clientListScrollPanel, BorderLayout.CENTER);

		JPanel chatRoomControlPanel = new JPanel();
		userPanel.add(chatRoomControlPanel, BorderLayout.CENTER);
		chatRoomControlPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblChatRoom = new JLabel("Chat Room:");
		chatRoomControlPanel.add(lblChatRoom, BorderLayout.NORTH);
		drawToolPanel.setLayout(new BorderLayout(0, 0));

		// TODO
		// need a panel subclass

		JPanel toolPanel = new JPanel();
		toolPanel.setBorder(new TitledBorder(null, "Tool Bar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		toolPanel.setLayout(new GridLayout(0, 1, 0, 0));
		toolPanel.setPreferredSize(new Dimension(0, 300));
		drawToolPanel.add(toolPanel, BorderLayout.NORTH);

		// Add tool bar button
		JButton btnTools = null;
		for (int i = 0; i < TOOLNAME.length; i++) {
			btnTools = new JButton(TOOLNAME[i]);
			btnTools.setCursor(new Cursor(Cursor.HAND_CURSOR));
			toolPanel.add(btnTools);
			btnTools.addActionListener(drawListener);
		}

		// Create color panel
		JPanel colorPanel = new JPanel();
		colorPanel.setBorder(new TitledBorder(null, "Color Bar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		drawToolPanel.add(colorPanel, BorderLayout.CENTER);
		colorPanel.setLayout(new BorderLayout(0, 0));

		JPanel currentColorPanel = new JPanel();
		colorPanel.add(currentColorPanel, BorderLayout.NORTH);
		currentColorPanel.setPreferredSize(new Dimension(0, 40));
		currentColorPanel.setLayout(new BorderLayout());

		JLabel lblCurrentColor = new JLabel(" Current:");
		currentColorPanel.add(lblCurrentColor, BorderLayout.CENTER);

		JPanel marginCC1Panel = new JPanel();
		marginCC1Panel.setPreferredSize(new Dimension(40, 40));
		currentColorPanel.add(marginCC1Panel, BorderLayout.EAST);

		btnCurrentColor = new JButton();
		marginCC1Panel.add(btnCurrentColor);
		btnCurrentColor.setBorderPainted(false);
		btnCurrentColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCurrentColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentColor = colorChooser.showDialog(frame, "Color Chooser", currentColor);
				btnCurrentColor.setBackground(currentColor);
			}
		});

		btnCurrentColor.setBackground(currentColor);
		btnCurrentColor.setOpaque(true);
		btnCurrentColor.setPreferredSize(new Dimension(30, 30));

		JPanel defaultColorPanel = new JPanel();
		colorPanel.add(defaultColorPanel, BorderLayout.CENTER);
		defaultColorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel thicknessPanel = new JPanel();
		thicknessPanel
				.setBorder(new TitledBorder(null, "Thickness Bar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		thicknessPanel.setPreferredSize(new Dimension(0, 50));
		drawToolPanel.add(thicknessPanel, BorderLayout.SOUTH);
		thicknessPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblThickness = new JLabel("Thickness:");
		thicknessPanel.add(lblThickness, BorderLayout.WEST);

		JPanel thicknessTextPanel = new JPanel();
		thicknessPanel.add(thicknessTextPanel, BorderLayout.CENTER);
		thicknessTextPanel.setLayout(new BorderLayout(0, 0));

		thicknessTextField = new JTextField();
		thicknessTextField.setDocument(new NumberTextField());
		thicknessTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thickness = Integer.parseInt(thicknessTextField.getText());  
				thicknessTextField.setFocusable(false);
				thicknessTextField.setFocusable(true);
			}
		});
		thicknessTextField.setText(Integer.toString(thickness));
		thicknessTextField.setHorizontalAlignment(SwingConstants.CENTER);
		thicknessTextPanel.add(thicknessTextField);
		thicknessTextField.setColumns(2);

		// Add default colors
		JButton btnDefaultColors = null;
		for (int i = 0; i < DEFAULTCOLORS.length; i++) {
			btnDefaultColors = new JButton();
			btnDefaultColors.setBorderPainted(false);
			btnDefaultColors.setBackground(DEFAULTCOLORS[i]);
			btnDefaultColors.setOpaque(true);
			btnDefaultColors.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnDefaultColors.setPreferredSize(new Dimension(40, 40));
			btnDefaultColors.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentColor = ((JButton) e.getSource()).getBackground();
					btnCurrentColor.setBackground(currentColor);
					System.out.println("Operation: Change color.");
				}
			});
			defaultColorPanel.add(btnDefaultColors);
		}

		// Add menu bar at the last, need to wait for creation of paintBoardPanel.
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.add(new FileMenu(this));
		menuBar.add(new EditMenu(this));

		frame.setVisible(true);

	}
}
