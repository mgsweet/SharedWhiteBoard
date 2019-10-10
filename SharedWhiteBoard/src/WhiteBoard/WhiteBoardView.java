package WhiteBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.DefaultColorSelectionModel;

public class WhiteBoardView {

	private JFrame frame;
	private JColorChooser colorChooser;
	private DrawListener drawListener;
	private PaintBoardPanel paintBoardPanel;
	private JButton btnCurrentColor;
	// Color
	private Color currentColor;
	private Color backgroundColor = null;

	private static Color[] defaultColors = { Color.BLACK, Color.BLUE, Color.WHITE, Color.GRAY, Color.RED, Color.GREEN,
			Color.ORANGE, Color.YELLOW, Color.PINK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.CYAN, Color.MAGENTA,
			new Color(250, 128, 114), new Color(210, 105, 30), new Color(160, 32, 240) };

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
	 * Get background color.
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WhiteBoardView window = new WhiteBoardView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WhiteBoardView() {
		currentColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		colorChooser = new JColorChooser(currentColor);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);
		frame.setTitle("white board");
		frame.setResizable(true);

		// Add Action Listener
		drawListener = new DrawListener(this);

		// Add draw tool panel.
		JPanel drawToolPanel = new JPanel();
		drawToolPanel.setPreferredSize(new Dimension(110, 0));
		frame.getContentPane().add(drawToolPanel, BorderLayout.WEST);

		// Add Painting broad.
		paintBoardPanel = new PaintBoardPanel();
		paintBoardPanel.setBackground(Color.white);
		paintBoardPanel.addMouseListener(drawListener);
		paintBoardPanel.addMouseMotionListener(drawListener);
		paintBoardPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		frame.getContentPane().add(paintBoardPanel, BorderLayout.CENTER);

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
		JButton btnPen = new JButton("pen");
		btnPen.setCursor(new Cursor(Cursor.HAND_CURSOR));
		toolPanel.add(btnPen);
		btnPen.addActionListener(drawListener);

		JButton btnLine = new JButton("line");
		btnLine.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLine.addActionListener(drawListener);
		toolPanel.add(btnLine);

		JButton btnCircle = new JButton("circle");
		btnCircle.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCircle.addActionListener(drawListener);
		toolPanel.add(btnCircle);

		JButton btnEraser = new JButton("eraser");
		btnEraser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEraser.addActionListener(drawListener);
		toolPanel.add(btnEraser);

		JButton btnRect = new JButton("rect");
		btnRect.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnRect.addActionListener(drawListener);
		toolPanel.add(btnRect);

		JButton btnOval = new JButton("oval");
		btnOval.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnOval.addActionListener(drawListener);
		toolPanel.add(btnOval);

		JButton btnRoundrect = new JButton("roundrect");
		btnRoundrect.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnRoundrect.addActionListener(drawListener);
		toolPanel.add(btnRoundrect);

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
				System.out.println("Operation: Change color.");
			}
		});

		btnCurrentColor.setBackground(currentColor);
		btnCurrentColor.setOpaque(true);
		btnCurrentColor.setPreferredSize(new Dimension(30, 30));

		JPanel defaultColorPanel = new JPanel();
		colorPanel.add(defaultColorPanel, BorderLayout.CENTER);
		defaultColorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Add 16 default color
		JButton tempButton = null;
		for (int i = 0; i < 16; i++) {
			tempButton = new JButton();
			tempButton.setBorderPainted(false);
			tempButton.setBackground(defaultColors[i]);
			tempButton.setOpaque(true);
			tempButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			tempButton.setPreferredSize(new Dimension(40, 40));
			tempButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentColor = ((JButton) e.getSource()).getBackground();
					btnCurrentColor.setBackground(currentColor);
					System.out.println("Operation: Change color.");
				}
			});
			defaultColorPanel.add(tempButton);
		}

		// Add menu bar at the last, need to wait for creation of paintBoardPanel.
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.add(new FileMenu(this));
		menuBar.add(new EditMenu(this));

		frame.setVisible(true);

	}
}
