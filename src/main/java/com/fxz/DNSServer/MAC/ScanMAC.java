package com.fxz.DNSServer.MAC;

import org.apache.log4j.Logger;
import org.dhcp4java.DHCPConstants;
import com.fxz.DNSServer.Utils.Params;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;
import jpcap.packet.UDPPacket;
import net.sourceforge.jpcap.net.EthernetPacket;
import net.sourceforge.jpcap.net.IPPacket;
import net.sourceforge.jpcap.net.RawPacket;
import net.sourceforge.jpcap.net.TCPPacket;

public class ScanMAC extends Thread {
	NetworkInterface device;
	String ipprefix;
	Logger logger = Logger.getLogger(ScanMAC.class);

	public ScanMAC(NetworkInterface device, String ipprefix) {
		this.device = device;
		this.ipprefix = ipprefix;
	}

	@Override
	public void run() {
		try {
			JpcapCaptor captor = JpcapCaptor.openDevice(device, 2000, false, 3000);
			captor.setFilter("udp or arp", true);
			new SendArp(captor, ipprefix, device).start();
			captor.loopPacket(-1, new PacketReceiver() {
				@Override
				public void receivePacket(Packet arg0) {
					if (arg0 != null && arg0.getClass().equals(UDPPacket.class)) {
						UDPPacket udpPacket = (UDPPacket) arg0;
						if (udpPacket.dst_port == 67) {
							DHCPPacket dhcppacket = DHCPPacket.getPacket(udpPacket);
							String hostname = dhcppacket.getOptionAsString(DHCPConstants.DHO_HOST_NAME);
							String requestIp = dhcppacket.getOptionAsString(DHCPConstants.DHO_DHCP_REQUESTED_ADDRESS);
							String mac = getMacFormat(dhcppacket.getChaddrAsHex());
							if (hostname != null && !mac.equalsIgnoreCase("N/A") && !hostname.equalsIgnoreCase("N/A")) {
								logger.info("Recv DHCP Packet HostName->" + hostname + " MAC->" + mac + " RequestIP->" + requestIp);
								Params.getRedis().setKey("MAC" + mac, hostname, 0);
							}
						}
					}
					if (arg0 != null && arg0.getClass().equals(ARPPacket.class)) {
						ARPPacket p = (ARPPacket) arg0;
						if (p.operation != ARPPacket.ARP_REPLY) {
							return;
						}
						boolean isTargetIP = false;
						if (p.target_protoaddr[0] == device.addresses[1].address.getAddress()[0] && p.target_protoaddr[1] == device.addresses[1].address.getAddress()[1] && p.target_protoaddr[2] == device.addresses[1].address.getAddress()[2] && p.target_protoaddr[3] == device.addresses[1].address.getAddress()[3]) {
							isTargetIP = true;
						}
						if (!isTargetIP) {
							return;
						}
						StringBuilder str = new StringBuilder();
						for (byte part : p.sender_protoaddr) {
							String hex = (part & 0xff) < 0 ? String.valueOf(part & 0xff + 256) : String.valueOf(part & 0xff);
							str.append(hex);
							str.append('.');
						}
						String ip = str.toString().substring(0, str.length() - 1);
						boolean isAlive = false;
						byte[] deadMac = stomac("00-00-00-00-00-00");
						if (!(p.target_hardaddr[0] == deadMac[0] && p.target_hardaddr[1] == deadMac[1] && p.target_hardaddr[2] == deadMac[2] && p.target_hardaddr[3] == deadMac[3] && p.target_hardaddr[4] == deadMac[4] && p.target_hardaddr[5] == deadMac[5])) {
							isAlive = true;
						}
						if (!isAlive) {
							return;
						}
						str = new StringBuilder();
						for (byte part : p.sender_hardaddr) {
							String hex = Integer.toHexString(part & 0xff).toUpperCase();
							str.append(hex.length() == 1 ? "0" + hex : hex);
							str.append('-');
						}
						String mac = str.toString().substring(0, 17);
						IP2MAC.addMAC(ip, mac);
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static String getMacFormat(String mac) {
		// B827EB649D3F
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < mac.length(); i += 2) {
			sBuilder.append(mac.substring(i, i + 2) + "-");
		}
		return sBuilder.toString().substring(0, sBuilder.toString().length() - 1);
	}

	private byte[] stomac(String s) {
		byte[] mac = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		String[] s1 = s.split("-");
		for (int x = 0; x < s1.length; x++) {
			mac[x] = (byte) ((Integer.parseInt(s1[x], 16)) & 0xff);
		}
		return mac;
	}

	public static void main(String[] args) {
		System.out.println(getMacFormat("01B827EB649D3F"));
	}
}
