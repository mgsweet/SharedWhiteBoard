package util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 23, 2019 5:37:47 PM
 */

public class RealIp {
	public static final String getHostIp() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {
//						System.out.println("本机的IP = " + ip.getHostAddress());
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
