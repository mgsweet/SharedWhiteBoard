package WhiteBoard;

public class ServerWhiteBoard extends SharedWhiteBoard {
	
	public ServerWhiteBoard() {
		super(PaintManager.SERVER_MODE);
		initRMI();
		initView();
	}
	
	public void initView() {
		String title = "Server-" + ip.getHostAddress() + ":" + registryPort;
		ui = new WhiteBoardView(this.paintManager, title);
	}
}
