package com.fxz.DNSServer.MAC;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.session.IoSession;
import com.fxz.DNSServer.Utils.Params;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import sun.net.util.IPAddressUtil;

/**
 * @ClassName: IP2MAC
 * @Description: 使用IP得到MAC和主机名
 * @author: Administrator
 * @date: 2018年8月15日 下午2:21:52
 */
public class IP2MAC {
	static Logger logger = Logger.getLogger(IP2MAC.class);
	// <IP,MAC>
	private static ConcurrentHashMap<String, String> ipmac = new ConcurrentHashMap<>();
	// <MAC,HOST>
	private static ConcurrentHashMap<String, String> machost = new ConcurrentHashMap<>();
	private static boolean isScanStarted = false;

	public static void addMAC(String ip, String mac) {
		ipmac.put(ip, mac);
		Params.getRedis().setKey("IP" + ip, mac, 60);
	}

	public static void addHost(String mac, String host) {
		machost.put(mac, host);
	}

	public static String getMAC(String ip) {
		String mac = Params.getRedis().getKey("IP" + ip);
		if (mac != null) {
			return mac;
		}
		return "N/A";
	}

	public static String getMAC(IoSession session) {
		String ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		return getMAC(ip);
	}

	public static String getHostName(IoSession session) {
		String ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		String mac = getMAC(session);
		String hostname = Params.getRedis().getKey("MAC" + mac);
		if (hostname != null && !hostname.equalsIgnoreCase("N/A")) {
			hostname = Params.getRedis().getKey("E" + ip);
		}
		if (hostname == null) {
			hostname = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostName();
			Params.getRedis().setKey("E" + ip, hostname, 60);
			if (!ip.equalsIgnoreCase(hostname) && !hostname.equalsIgnoreCase("N/A") && !mac.equalsIgnoreCase("N/A")) {
				Params.getRedis().setKey("MAC" + mac, hostname, 0);
			}
		}
		return hostname;
	}

	public static String getHostName(String ip) {
		String mac = Params.getRedis().getKey("IP" + ip);
		if (mac == null) {
			return ip;
		}
		String hostname = Params.getRedis().getKey("MAC" + mac);
		if (hostname == null) {
			return ip;
		}
		return hostname;

	}

	private static HashMap<String, ScanMAC> scanMap = new HashMap<>();

	public static void stop() {
		if (scanMap.size() == 0) {
			return;
		}
		for (Entry<String, ScanMAC> scanthread : scanMap.entrySet()) {
			ScanMAC threadmac = scanthread.getValue();
			try {
				threadmac.interrupt();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		isScanStarted=false;
	}

	public static void startScanMac() {
		if (!isScanStarted) {
			isScanStarted = true;
			NetworkInterface[] devices = JpcapCaptor.getDeviceList();
			for (NetworkInterface inter : devices) {
				NetworkInterfaceAddress[] interfaces = inter.addresses;
				StringBuilder str = new StringBuilder();
				for (byte part : inter.mac_address) {
					String hex = Integer.toHexString(part & 0xff).toUpperCase();
					str.append(hex.length() == 1 ? "0" + hex : hex);
					str.append('-');
				}
				String mac = str.toString().substring(0, 17);
				logger.info("Desc->" + inter.datalink_description + "\r\nMAC->" + mac + "\r\n----------------------------------------");
				for (NetworkInterfaceAddress networkInterfaceAddress : interfaces) {
					logger.info("IP->" + networkInterfaceAddress.address.getHostAddress() + "\r\n" + "isLookback->" + networkInterfaceAddress.address.isLoopbackAddress() + "\r\n" + "isLinkLoacl->" + networkInterfaceAddress.address.isLinkLocalAddress() + "\r\nisAnyLocal->" + networkInterfaceAddress.address.isAnyLocalAddress());
					if (!networkInterfaceAddress.address.getHostAddress().equalsIgnoreCase("0.0.0.0") && IPAddressUtil.isIPv4LiteralAddress(networkInterfaceAddress.address.getHostAddress())) {
						IP2MAC.addMAC(networkInterfaceAddress.address.getHostAddress(), mac);
						logger.info("Start Scan....");
						String ip = networkInterfaceAddress.address.getHostAddress();
						String ipprefix = ip.substring(0, ip.lastIndexOf("."));
						logger.info("IPprefix->" + ipprefix);
						ScanMAC scanMAC = new ScanMAC(inter, ipprefix);
						scanMap.put(UUID.randomUUID().toString(), scanMAC);
						scanMAC.start();
					}
				}
			}
		} else {
			logger.info("Scan Mac Started!");
		}
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config/log4j.properties");
		IP2MAC.startScanMac();
		IP2MAC.startScanMac();
	}
}
