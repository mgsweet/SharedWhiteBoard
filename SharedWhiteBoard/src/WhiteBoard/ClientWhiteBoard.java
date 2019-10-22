package WhiteBoard;

import App.App;
import Chat.ChatClient;
import ClientUser.UserManager;

public class ClientWhiteBoard extends SharedWhiteBoard {
	private ChatClient chatClient;
	
	public ClientWhiteBoard(App app, String hostId, String hostIp, int registerPort) {
		super(app);
		initManager(hostId, hostIp, registerPort);
		initUmRMI();
		initPaintRMI();
		initView();
	}
	
	private void initView() {
		String title = "Client-" + app.getIp() + ":" + app.getRegistryPort();
		ui = new WhiteBoardView(app, this.paintManager, userManager, title);
	}
	
	public void createChatClient(String hostIp, int chatPort) {
		chatClient = new ChatClient(app.getUserId(), hostIp, chatPort);
		ui.setChatPanel(chatClient.getPanel());
	}
	
	private void initManager(String hostId, String hostIp, int registerPort) {
		userManager = new UserManager(false, hostId, hostIp, registerPort, -1);
		paintManager = new PaintManager(paintManager.CLIENT_MODE, userManager);
	}
	
}