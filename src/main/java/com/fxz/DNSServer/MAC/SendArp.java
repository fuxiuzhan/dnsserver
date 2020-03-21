package com.fxz.DNSServer.MAC;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.*;

public class SendArp extends Thread {
	JpcapCaptor jcap;
	String ipprefix;
	NetworkInterface device;

	public SendArp(JpcapCaptor icap, String prefix, NetworkInterface device) {
		this.jcap = icap;
		this.ipprefix = prefix;
		this.device = device;
	}

	@Override
	public void run() {
		JpcapSender sender = jcap.getJpcapSenderInstance();
		ArrayList<String> list = new ArrayList<>();
		for (int i = 1; i < 256; i++) {
			list.add(this.ipprefix + "." + i);
		}
		int i = 1;
		while (true) {
			i = i % 255;
			try {
				if (i == 0) {
					Thread.sleep(5 * 60 * 1000);
				}
				i++;
				ARPPacket arpPacket = constractRequestArp(device, list.get(i % 255));
				sender.sendPacket(arpPacket);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	
	private ARPPacket constractRequestArp(NetworkInterface device, String IP) throws UnknownHostException {
		byte[] broadcast = stomac("ff-ff-ff-ff-ff-ff");
		InetAddress srcip = device.addresses[1].address;
		InetAddress desip = InetAddress.getByName(IP);
		ARPPacket arpPacket = new ARPPacket();
		arpPacket.hardtype = ARPPacket.HARDTYPE_ETHER;
		arpPacket.prototype = ARPPacket.PROTOTYPE_IP;
		arpPacket.operation = ARPPacket.ARP_REQUEST;
		arpPacket.hlen = 6;
		arpPacket.plen = 4;
		arpPacket.sender_hardaddr = device.mac_address;
		arpPacket.sender_protoaddr = srcip.getAddress();
		arpPacket.target_hardaddr = broadcast;
		arpPacket.target_protoaddr = desip.getAddress();
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac = device.mac_address;
		ether.dst_mac = broadcast;
		arpPacket.datalink = ether;
		/*
		 * TCP协议在通讯中主要使用seq和ack来确认和交换信息，TCP安全的保证依赖于顺序号和确认号，如果在主机发出数据时将数据
		 * 截获就可以实现会话的劫持，但是在Linux中开启内核的数据转发实现路由却不能在用户态拦截数据，如果想要拦截数据，怕是
		 * 要增加控制接口了，由于主机发出数据到接收数据需要一定的延时，如果你作为中间路由，只需要少量代码也可以中途改变连接
		 * 也就是当主机发出请求时，作为路由的你会收到数据包，内核会转发数据包，此时你可以拿到顺序号和确认号，如果是http请求
		 * 则可以直接封装跳转代码，然后发送FYN数据包或者RST数据包，主机会将数据提交高层应用处理，而后来的真正数据由于顺序号
		 * 和确认号已经不可用，会被当成重复数据包处理。
		 * 这就实现了http的跳转
		 * 也可以伪装明文TCP通讯
		 */
		//jpcap.packet.TCPPacket tcpPacket=new jpcap.packet.TCPPacket(priority, priority, eetop, eetop, daemon, daemon, daemon, daemon, daemon, daemon, daemon, daemon, priority, priority);
		//tcpPacket.datalink=ether;
		return arpPacket;
	}

	private byte[] stomac(String s) {
		byte[] mac = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		String[] s1 = s.split("-");
		for (int x = 0; x < s1.length; x++) {
			mac[x] = (byte) ((Integer.parseInt(s1[x], 16)) & 0xff);
		}

		return mac;
	}
}
