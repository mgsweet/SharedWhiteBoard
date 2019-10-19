package WhiteBoard;

import Client.Client;

public class ServerWhiteBoard extends SharedWhiteBoard {
	
	public ServerWhiteBoard(Client client) {
		super(client, PaintManager.SERVER_MODE);
		initRMI();
		initView();
	}
	
	public void initView() {
		String title = "Server-" + ip.getHostAddress() + ":" + registryPort;
		ui = new WhiteBoardView(client, this.paintManager, title);
	}
}
