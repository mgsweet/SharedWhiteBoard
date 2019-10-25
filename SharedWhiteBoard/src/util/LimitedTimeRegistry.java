package util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 25, 2019 1:18:10 PM
 */

public class LimitedTimeRegistry {
	public static final Registry getLimitedTimeRegistry(String ip, int port, int timeout) throws RemoteException {
		return LocateRegistry.getRegistry(ip, port, new RMIClientSocketFactory() {
			@Override
			public Socket createSocket(String host, int port) throws IOException {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 2000);
			return socket;
		}});
	}
}
