package WhiteBoard;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private Color currentColor;
	private DrawListener drawListener;
	private PaintBoardPanel paintBoardPanel;
	private JButton btnCurrentColor;
	
	private static Color[] defaultColors = {
			Color.BLACK, Color.BLUE, Color.WHITE, Color.GRAY,
			Color.RED, Color.GREEN, Color.ORANGE, Color.YELLOW,
			Color.PINK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.CYAN,
			Color.MAGENTA, new Color(250, 128, 114), new Color(210, 105, 30), new Color(160, 32, 240)
	};
	
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
		colorChooser = new JColorChooser(currentColor);
		initialize();
	}

	/**
	 * Create the file menu in the menu bar.
	 */
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File(F)");
		menu.setMnemonic(KeyEvent.VK_F); // 设置快速访问符
		JMenuItem item = new JMenuItem("New(N)", KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menu.add(item);
		item = new JMenuItem("Open(O)", KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menu.add(item);
		item = new JMenuItem("Save(S)", KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Exit(E)", KeyEvent.VK_E);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menu.add(item);
		return menu;
	}

	/**
	 * Create the edit menu in the menu bar.
	 */
	private JMenu createEditMenu() {
		JMenu menu = new JMenu("Edit(E)");
		menu.setMnemonic(KeyEvent.VK_E);
		JMenuItem item = new JMenuItem("Undo(U)", KeyEvent.VK_U);
		item.setEnabled(false);
		menu.add(item);
		return menu;
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
		
		// Add menu bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());

		// Add draw tool panel.
		JPanel drawToolPanel = new JPanel();
		drawToolPanel.setPreferredSize(new Dimension(110, 0));
		frame.getContentPane().add(drawToolPanel, BorderLayout.WEST);
		
		paintBoardPanel = new PaintBoardPanel();
		paintBoardPanel.setBackground(Color.white);
		paintBoardPanel.addMouseListener(drawListener);
		paintBoardPanel.addMouseMotionListener(drawListener);
		frame.getContentPane().add(paintBoardPanel, BorderLayout.CENTER);
		
		JPanel chatPanel = new JPanel();
		chatPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		chatPanel.setPreferredSize(new Dimension(200, 600));
		chatPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(chatPanel, BorderLayout.EAST);
		
		JLabel lab1 = new JLabel("chatting room");
		chatPanel.add(lab1);
		drawToolPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel toolPanel = new JPanel();
		toolPanel.setBorder(new TitledBorder(null, "Tool Bar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		toolPanel.setLayout(new GridLayout(0, 1, 0, 0));
		toolPanel.setPreferredSize(new Dimension(0, 300));
		drawToolPanel.add(toolPanel, BorderLayout.NORTH);

		// Add tool bar button
		JButton btnPen = new JButton("pen");
		toolPanel.add(btnPen);
		btnPen.addActionListener(drawListener);
		
		JButton btnLine = new JButton("line");
		btnLine.addActionListener(drawListener);
		toolPanel.add(btnLine);

		JButton btnCircle = new JButton("circle");
		btnCircle.addActionListener(drawListener);
		toolPanel.add(btnCircle);

		JButton btnSpray = new JButton("spray");
		btnSpray.addActionListener(drawListener);
		toolPanel.add(btnSpray);

		JButton btnEraser = new JButton("eraser");
		btnEraser.addActionListener(drawListener);
		toolPanel.add(btnEraser);

		JButton btnRect = new JButton("rect");
		btnRect.addActionListener(drawListener);
		toolPanel.add(btnRect);

		JButton btnOval = new JButton("oval");
		btnOval.addActionListener(drawListener);
		toolPanel.add(btnOval);

		JButton btnRoundrect = new JButton("roundrect");
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
		btnCurrentColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorChooser.showDialog(frame, "Color Chooser", currentColor);
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
			tempButton.setPreferredSize(new Dimension(40, 40));
			tempButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentColor = ((JButton)e.getSource()).getBackground();
					btnCurrentColor.setBackground(currentColor);
					System.out.print(currentColor);
				}
			});
			defaultColorPanel.add(tempButton);
		}

		frame.setVisible(true);

	}
}
