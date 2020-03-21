package com.fxz.DNSServer.MAC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dhcp4java.DHCPBadPacketException;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.HardwareAddress;
import jpcap.packet.UDPPacket;

public class DHCPPacket implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DHCPPacket.class.getName().toLowerCase());
	private String comment;
	private byte op;
	private byte htype;
	private byte hlen;
	private byte hops;
	private int xid;
	private short secs;
	private short flags;
	private byte[] ciaddr;
	private byte[] yiaddr;
	private byte[] siaddr;
	private byte[] giaddr;
	private byte[] chaddr;
	private byte[] sname;
	private byte[] file;
	private Map<Byte, DHCPOption> options;
	private boolean isDhcp;
	private boolean truncated;
	private byte[] padding;
	private InetAddress address;
	private int port;
	private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public DHCPPacket() {
		this.comment = "";
		this.op = 2;
		this.htype = 1;
		this.hlen = 6;
		this.ciaddr = new byte[4];
		this.yiaddr = new byte[4];
		this.siaddr = new byte[4];
		this.giaddr = new byte[4];
		this.chaddr = new byte[16];
		this.sname = new byte[64];
		this.file = new byte[''];
		this.padding = new byte[0];
		this.isDhcp = true;
		this.options = new LinkedHashMap();
	}

	public static DHCPPacket getPacket(DatagramPacket datagram) throws DHCPBadPacketException {
		if (datagram == null) {
			throw new IllegalArgumentException("datagram is null");
		}
		DHCPPacket packet = new DHCPPacket();

		packet.marshall(datagram.getData(), datagram.getOffset(), datagram.getLength(), datagram.getAddress(), datagram.getPort(), true);

		return packet;
	}

	public static DHCPPacket getPacket(UDPPacket udpPacket) {
		if (udpPacket == null) {
			throw new IllegalArgumentException("datagram is null");
		}
		DHCPPacket packet = new DHCPPacket();
		packet.marshall(udpPacket.data, udpPacket.offset, udpPacket.data.length, udpPacket.src_ip, udpPacket.src_port, true);
		return packet;

	}

	public static DHCPPacket getPacket(byte[] buf, int offset, int length, boolean strict) throws DHCPBadPacketException {
		DHCPPacket packet = new DHCPPacket();

		packet.marshall(buf, offset, length, null, 0, strict);
		return packet;
	}

	public DHCPPacket clone() {
		try {
			DHCPPacket p = (DHCPPacket) super.clone();

			p.ciaddr = ((byte[]) this.ciaddr.clone());
			p.yiaddr = ((byte[]) this.yiaddr.clone());
			p.siaddr = ((byte[]) this.siaddr.clone());
			p.giaddr = ((byte[]) this.giaddr.clone());
			p.chaddr = ((byte[]) this.chaddr.clone());
			p.sname = ((byte[]) this.sname.clone());
			p.file = ((byte[]) this.file.clone());

			p.options = new LinkedHashMap(this.options);
			p.padding = ((byte[]) this.padding.clone());

			p.truncated = false;

			return p;
		} catch (CloneNotSupportedException e) {
		}
		throw new InternalError();
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof DHCPPacket)) {
			return false;
		}

		DHCPPacket p = (DHCPPacket) o;

		boolean b = this.comment.equals(p.comment);
		b &= this.op == p.op;
		b &= this.htype == p.htype;
		b &= this.hlen == p.hlen;
		b &= this.hops == p.hops;
		b &= this.xid == p.xid;
		b &= this.secs == p.secs;
		b &= this.flags == p.flags;
		b &= Arrays.equals(this.ciaddr, p.ciaddr);
		b &= Arrays.equals(this.yiaddr, p.yiaddr);
		b &= Arrays.equals(this.siaddr, p.siaddr);
		b &= Arrays.equals(this.giaddr, p.giaddr);
		b &= Arrays.equals(this.chaddr, p.chaddr);
		b &= Arrays.equals(this.sname, p.sname);
		b &= Arrays.equals(this.file, p.file);
		b &= this.options.equals(p.options);
		b &= this.isDhcp == p.isDhcp;

		b &= Arrays.equals(this.padding, p.padding);
		b &= equalsStatic(this.address, p.address);
		b &= this.port == p.port;

		return b;
	}

	public int hashCode() {
		int h = -1;
		h ^= this.comment.hashCode();
		h += this.op;
		h += this.htype;
		h += this.hlen;
		h += this.hops;
		h += this.xid;
		h += this.secs;
		h ^= this.flags;
		h ^= Arrays.hashCode(this.ciaddr);
		h ^= Arrays.hashCode(this.yiaddr);
		h ^= Arrays.hashCode(this.siaddr);
		h ^= Arrays.hashCode(this.giaddr);
		h ^= Arrays.hashCode(this.chaddr);
		h ^= Arrays.hashCode(this.sname);
		h ^= Arrays.hashCode(this.file);
		h ^= this.options.hashCode();
		h += (this.isDhcp ? 1 : 0);

		h ^= Arrays.hashCode(this.padding);
		h ^= (this.address != null ? this.address.hashCode() : 0);
		h += this.port;
		return h;
	}

	private static boolean equalsStatic(Object a, Object b) {
		return a == null ? false : b == null ? true : a.equals(b);
	}

	private void assertInvariants() {
		assert (this.comment != null);
		assert (this.ciaddr != null);
		assert (this.ciaddr.length == 4);
		assert (this.yiaddr != null);
		assert (this.yiaddr.length == 4);
		assert (this.siaddr != null);
		assert (this.siaddr.length == 4);
		assert (this.giaddr != null);
		assert (this.giaddr.length == 4);

		assert (this.chaddr != null);
		assert (this.chaddr.length == 16);
		assert (this.sname != null);
		assert (this.sname.length == 64);
		assert (this.file != null);
		assert (this.file.length == 128);
		assert (this.padding != null);

		assert (this.options != null);
		for (Map.Entry mapEntry : this.options.entrySet()) {
			Byte key = (Byte) mapEntry.getKey();
			DHCPOption opt = (DHCPOption) mapEntry.getValue();

			assert (key != null);
			assert (key.byteValue() != 0);
			assert (key.byteValue() != -1);
			assert (opt != null);
			assert (opt.getCode() == key.byteValue());
			assert (opt.getValueFast() != null);
		}
	}

	protected DHCPPacket marshall(byte[] buffer, int offset, int length, InetAddress address0, int port0, boolean strict) {
		if (buffer == null) {
			throw new IllegalArgumentException("null buffer not allowed");
		}
		if (offset < 0) {
			throw new IndexOutOfBoundsException("negative offset not allowed");
		}
		if (length < 0) {
			throw new IllegalArgumentException("negative length not allowed");
		}
		if (buffer.length < offset + length) {
			throw new IndexOutOfBoundsException("offset+length exceeds buffer length");
		}

		if (length < 236) {
			throw new DHCPBadPacketException("DHCP Packet too small (" + length + ") absolute minimum is " + 236);
		}

		if (length > 1500) {
			throw new DHCPBadPacketException("DHCP Packet too big (" + length + ") max MTU is " + 1500);
		}

		this.address = address0;
		this.port = port0;
		try {
			ByteArrayInputStream inBStream = new ByteArrayInputStream(buffer, offset, length);
			DataInputStream inStream = new DataInputStream(inBStream);

			this.op = inStream.readByte();
			this.htype = inStream.readByte();
			this.hlen = inStream.readByte();
			this.hops = inStream.readByte();
			this.xid = inStream.readInt();
			this.secs = inStream.readShort();
			this.flags = inStream.readShort();
			inStream.readFully(this.ciaddr, 0, 4);
			inStream.readFully(this.yiaddr, 0, 4);
			inStream.readFully(this.siaddr, 0, 4);
			inStream.readFully(this.giaddr, 0, 4);
			inStream.readFully(this.chaddr, 0, 16);
			inStream.readFully(this.sname, 0, 64);
			inStream.readFully(this.file, 0, 128);

			this.isDhcp = true;
			inBStream.mark(4);
			if (inStream.readInt() != 1669485411) {
				this.isDhcp = false;
				inBStream.reset();
			}

			if (this.isDhcp) {
				int type = 0;
				while (true) {
					int r = inBStream.read();
					if (r < 0)
						break;
					type = (byte) r;

					if (type != 0) {
						if (type == -1)
							break;
						r = inBStream.read();
						if (r < 0)
							break;
						int len = Math.min(r, inBStream.available());
						byte[] unit_opt = new byte[len];
						inBStream.read(unit_opt);

						setOption(new DHCPOption((byte) type, unit_opt));
					}
				}
				this.truncated = (type != -1);
				if ((strict) && (this.truncated)) {
					throw new DHCPBadPacketException("Packet seams to be truncated");
				}

			}

			this.padding = new byte[inBStream.available()];
			inBStream.read(this.padding);

			assertInvariants();

			return this;
		} catch (IOException e) {
			throw new DHCPBadPacketException("IOException: " + e.toString(), e);
		}
	}

	public byte[] serialize() {
		int minLen = 236;

		if (this.isDhcp) {
			minLen += 64;
		}

		return serialize(minLen, 576);
	}

	public byte[] serialize(int minSize, int maxSize) {
		assertInvariants();

		ByteArrayOutputStream outBStream = new ByteArrayOutputStream(750);
		DataOutputStream outStream = new DataOutputStream(outBStream);
		try {
			outStream.writeByte(this.op);
			outStream.writeByte(this.htype);
			outStream.writeByte(this.hlen);
			outStream.writeByte(this.hops);
			outStream.writeInt(this.xid);
			outStream.writeShort(this.secs);
			outStream.writeShort(this.flags);
			outStream.write(this.ciaddr, 0, 4);
			outStream.write(this.yiaddr, 0, 4);
			outStream.write(this.siaddr, 0, 4);
			outStream.write(this.giaddr, 0, 4);
			outStream.write(this.chaddr, 0, 16);
			outStream.write(this.sname, 0, 64);
			outStream.write(this.file, 0, 128);

			if (this.isDhcp) {
				outStream.writeInt(1669485411);

				for (DHCPOption opt : getOptionsCollection()) {
					assert (opt != null);
					assert (opt.getCode() != 0);
					assert (opt.getCode() != -1);
					assert (opt.getValueFast() != null);
					int size = opt.getValueFast().length;
					assert (size >= 0);
					if (size > 255) {
						throw new DHCPBadPacketException("Options larger than 255 bytes are not yet supported");
					}
					outStream.writeByte(opt.getCode());
					outStream.writeByte(size);
					outStream.write(opt.getValueFast());
				}

				outStream.writeByte(-1);
			}

			outStream.write(this.padding);

			int min_padding = minSize - outBStream.size();
			if (min_padding > 0) {
				byte[] add_padding = new byte[min_padding];
				outStream.write(add_padding);
			}

			byte[] data = outBStream.toByteArray();

			if (data.length > 1500) {
				throw new DHCPBadPacketException("serialize: packet too big (" + data.length + " greater than max MAX_MTU (" + 1500 + ')');
			}

			return data;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unexpected Exception", e);
			throw new DHCPBadPacketException("IOException raised: " + e.toString());
		}
	}
	public String getMacAddr()
	{
		StringBuilder sBuilder=new StringBuilder();
		appendChaddrAsHex(sBuilder);
		return sBuilder.toString();
	}
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		try {
			buffer.append(this.isDhcp ? "DHCP Packet" : "BOOTP Packet").append("\ncomment=").append(this.comment).append("\naddress=").append(this.address != null ? this.address.getHostAddress() : "").append('(').append(this.port).append(')').append("\nop=");
			buffer.append("\nhtype=");
			buffer.append("\nhlen=").append(this.hlen).append("\nhops=").append(this.hops).append("\nxid=0x");
			appendHex(buffer, this.xid);
			buffer.append("\nsecs=").append(this.secs).append("\nflags=0x").append(Integer.toHexString(this.flags)).append("\nciaddr=");
			appendHostAddress(buffer, InetAddress.getByAddress(this.ciaddr));
			buffer.append("\nyiaddr=");
			appendHostAddress(buffer, InetAddress.getByAddress(this.yiaddr));
			buffer.append("\nsiaddr=");
			appendHostAddress(buffer, InetAddress.getByAddress(this.siaddr));
			buffer.append("\ngiaddr=");
			appendHostAddress(buffer, InetAddress.getByAddress(this.giaddr));
			buffer.append("\nchaddr=0x");
			appendChaddrAsHex(buffer);
			buffer.append("\nsname=").append(getSname()).append("\nfile=").append(getFile());
			if (this.isDhcp) {
				buffer.append("\nOptions follows:");
				for (DHCPOption opt : getOptionsCollection()) {
					buffer.append('\n');
					opt.append(buffer);
				}
			}
			buffer.append("\npadding[").append(this.padding.length).append("]=");

			appendHex(buffer, this.padding);
		} catch (Exception e) {
		}
		return buffer.toString();
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte[] getChaddr() {
		return (byte[]) this.chaddr.clone();
	}

	private StringBuilder appendChaddrAsHex(StringBuilder buffer) {
		appendHex(buffer, this.chaddr, 0, this.hlen & 0xFF);
		return buffer;
	}

	public HardwareAddress getHardwareAddress() {
		int len = this.hlen & 0xFF;
		if (len > 16) {
			len = 16;
		}
		byte[] buf = new byte[len];
		System.arraycopy(this.chaddr, 0, buf, 0, len);
		return new HardwareAddress(this.htype, buf);
	}

	public String getChaddrAsHex() {
		return appendChaddrAsHex(new StringBuilder(this.hlen & 0xFF)).toString();
	}

	public void setChaddr(byte[] chaddr) {
		if (chaddr != null) {
			if (chaddr.length > this.chaddr.length) {
				throw new IllegalArgumentException("chaddr is too long: " + chaddr.length + ", max is: " + this.chaddr.length);
			}

			Arrays.fill(this.chaddr, (byte) 0);
			System.arraycopy(chaddr, 0, this.chaddr, 0, chaddr.length);
		} else {
			Arrays.fill(this.chaddr, (byte) 0);
		}
	}

	public void setChaddrHex(String hex) {
		setChaddr(hex2Bytes(hex));
	}

	public InetAddress getCiaddr() {
		try {
			return InetAddress.getByAddress(getCiaddrRaw());
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "Unexpected UnknownHostException", e);
		}
		return null;
	}

	public byte[] getCiaddrRaw() {
		return (byte[]) this.ciaddr.clone();
	}

	public void setCiaddr(InetAddress ciaddr) {
		if (!(ciaddr instanceof Inet4Address)) {
			throw new IllegalArgumentException("Inet4Address required");
		}
		setCiaddrRaw(ciaddr.getAddress());
	}

	public void setCiaddr(String ciaddr) throws UnknownHostException {
		setCiaddr(InetAddress.getByName(ciaddr));
	}

	public void setCiaddrRaw(byte[] ciaddr) {
		if (ciaddr.length != 4) {
			throw new IllegalArgumentException("4-byte array required");
		}
		System.arraycopy(ciaddr, 0, this.ciaddr, 0, 4);
	}

	public byte[] getFileRaw() {
		return (byte[]) this.file.clone();
	}

	public String getFile() {
		return bytesToString(getFileRaw());
	}

	public void setFile(String file) {
		setFileRaw(stringToBytes(file));
	}

	public void setFileRaw(byte[] file) {
		if (file != null) {
			if (file.length > this.file.length) {
				throw new IllegalArgumentException("File is too long:" + file.length + " max is:" + this.file.length);
			}
			Arrays.fill(this.file, (byte) 0);
			System.arraycopy(file, 0, this.file, 0, file.length);
		} else {
			Arrays.fill(this.file, (byte) 0);
		}
	}

	public short getFlags() {
		return this.flags;
	}

	public void setFlags(short flags) {
		this.flags = flags;
	}

	public InetAddress getGiaddr() {
		try {
			return InetAddress.getByAddress(getGiaddrRaw());
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "Unexpected UnknownHostException", e);
		}
		return null;
	}

	public byte[] getGiaddrRaw() {
		return (byte[]) this.giaddr.clone();
	}

	public void setGiaddr(InetAddress giaddr) {
		if (!(giaddr instanceof Inet4Address)) {
			throw new IllegalArgumentException("Inet4Address required");
		}
		setGiaddrRaw(giaddr.getAddress());
	}

	public void setGiaddr(String giaddr) throws UnknownHostException {
		setGiaddr(InetAddress.getByName(giaddr));
	}

	public void setGiaddrRaw(byte[] giaddr) {
		if (giaddr.length != 4) {
			throw new IllegalArgumentException("4-byte array required");
		}
		System.arraycopy(giaddr, 0, this.giaddr, 0, 4);
	}

	public byte getHlen() {
		return this.hlen;
	}

	public void setHlen(byte hlen) {
		this.hlen = hlen;
	}

	public byte getHops() {
		return this.hops;
	}

	public void setHops(byte hops) {
		this.hops = hops;
	}

	public byte getHtype() {
		return this.htype;
	}

	public void setHtype(byte htype) {
		this.htype = htype;
	}

	public boolean isDhcp() {
		return this.isDhcp;
	}

	public void setDhcp(boolean isDhcp) {
		this.isDhcp = isDhcp;
	}

	public byte getOp() {
		return this.op;
	}

	public void setOp(byte op) {
		this.op = op;
	}

	public byte[] getPadding() {
		return (byte[]) this.padding.clone();
	}

	public void setPadding(byte[] padding) {
		this.padding = (padding == null ? new byte[0] : (byte[]) padding.clone());
	}

	public void setPaddingWithZeroes(int length) {
		if (length < 0) {
			length = 0;
		}
		if (length > 1500) {
			throw new IllegalArgumentException("length is > 1500");
		}
		setPadding(new byte[length]);
	}

	public short getSecs() {
		return this.secs;
	}

	public void setSecs(short secs) {
		this.secs = secs;
	}

	public InetAddress getSiaddr() {
		try {
			return InetAddress.getByAddress(getSiaddrRaw());
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "Unexpected UnknownHostException", e);
		}
		return null;
	}

	public byte[] getSiaddrRaw() {
		return (byte[]) this.siaddr.clone();
	}

	public void setSiaddr(InetAddress siaddr) {
		if (!(siaddr instanceof Inet4Address)) {
			throw new IllegalArgumentException("Inet4Address required");
		}
		setSiaddrRaw(siaddr.getAddress());
	}

	public void setSiaddr(String siaddr) throws UnknownHostException {
		setSiaddr(InetAddress.getByName(siaddr));
	}

	public void setSiaddrRaw(byte[] siaddr) {
		if (siaddr.length != 4) {
			throw new IllegalArgumentException("4-byte array required");
		}
		System.arraycopy(siaddr, 0, this.siaddr, 0, 4);
	}

	public byte[] getSnameRaw() {
		return (byte[]) this.sname.clone();
	}

	public String getSname() {
		return bytesToString(getSnameRaw());
	}

	public void setSname(String sname) {
		setSnameRaw(stringToBytes(sname));
	}

	public void setSnameRaw(byte[] sname) {
		if (sname != null) {
			if (sname.length > this.sname.length) {
				throw new IllegalArgumentException("Sname is too long:" + sname.length + " max is:" + this.sname.length);
			}
			Arrays.fill(this.sname, (byte) 0);
			System.arraycopy(sname, 0, this.sname, 0, sname.length);
		} else {
			Arrays.fill(this.sname, (byte) 0);
		}
	}

	public int getXid() {
		return this.xid;
	}

	public void setXid(int xid) {
		this.xid = xid;
	}

	public InetAddress getYiaddr() {
		try {
			return InetAddress.getByAddress(getYiaddrRaw());
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, "Unexpected UnknownHostException", e);
		}
		return null;
	}

	public byte[] getYiaddrRaw() {
		return (byte[]) this.yiaddr.clone();
	}

	public void setYiaddr(InetAddress yiaddr) {
		if (!(yiaddr instanceof Inet4Address)) {
			throw new IllegalArgumentException("Inet4Address required");
		}
		setYiaddrRaw(yiaddr.getAddress());
	}

	public void setYiaddr(String yiaddr) throws UnknownHostException {
		setYiaddr(InetAddress.getByName(yiaddr));
	}

	public void setYiaddrRaw(byte[] yiaddr) {
		if (yiaddr.length != 4) {
			throw new IllegalArgumentException("4-byte array required");
		}
		System.arraycopy(yiaddr, 0, this.yiaddr, 0, 4);
	}

	public Byte getDHCPMessageType() {
		return getOptionAsByte((byte) 53);
	}

	public void setDHCPMessageType(byte optionType) {
		setOptionAsByte((byte) 53, optionType);
	}

	public boolean isTruncated() {
		return this.truncated;
	}

	public Integer getOptionAsNum(byte code) {
		DHCPOption opt = getOption(code);
		return opt != null ? opt.getValueAsNum() : null;
	}

	public Byte getOptionAsByte(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : Byte.valueOf(opt.getValueAsByte());
	}

	public Short getOptionAsShort(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : Short.valueOf(opt.getValueAsShort());
	}

	public Integer getOptionAsInteger(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : Integer.valueOf(opt.getValueAsInt());
	}

	public InetAddress getOptionAsInetAddr(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : opt.getValueAsInetAddr();
	}

	public String getOptionAsString(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : opt.getValueAsString();
	}

	public short[] getOptionAsShorts(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : opt.getValueAsShorts();
	}

	public InetAddress[] getOptionAsInetAddrs(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : opt.getValueAsInetAddrs();
	}

	public byte[] getOptionAsBytes(byte code) throws IllegalArgumentException {
		DHCPOption opt = getOption(code);
		return opt == null ? null : opt.getValueAsBytes();
	}

	public void setOptionAsByte(byte code, byte val) {
		setOption(DHCPOption.newOptionAsByte(code, val));
	}

	public void setOptionAsShort(byte code, short val) {
		setOption(DHCPOption.newOptionAsShort(code, val));
	}

	public void setOptionAsInt(byte code, int val) {
		setOption(DHCPOption.newOptionAsInt(code, val));
	}

	public void setOptionAsInetAddress(byte code, InetAddress val) {
		setOption(DHCPOption.newOptionAsInetAddress(code, val));
	}

	public void setOptionAsInetAddress(byte code, String val) throws UnknownHostException {
		setOption(DHCPOption.newOptionAsInetAddress(code, InetAddress.getByName(val)));
	}

	public void setOptionAsInetAddresses(byte code, InetAddress[] val) {
		setOption(DHCPOption.newOptionAsInetAddresses(code, val));
	}

	public void setOptionAsString(byte code, String val) {
		setOption(DHCPOption.newOptionAsString(code, val));
	}

	public byte[] getOptionRaw(byte code) {
		DHCPOption opt = getOption(code);
		return opt == null ? null : opt.getValueFast();
	}

	public DHCPOption getOption(byte code) {
		DHCPOption opt = (DHCPOption) this.options.get(Byte.valueOf(code));

		if (opt == null) {
			return null;
		}
		assert (opt.getCode() == code);
		assert (opt.getValueFast() != null);
		return opt;
	}

	public boolean containsOption(byte code) {
		return this.options.containsKey(Byte.valueOf(code));
	}

	public Collection<DHCPOption> getOptionsCollection() {
		return Collections.unmodifiableCollection(this.options.values());
	}

	public DHCPOption[] getOptionsArray() {
		return (DHCPOption[]) this.options.values().toArray(new DHCPOption[this.options.size()]);
	}

	public void setOptionRaw(byte code, byte[] buf) {
		if (buf == null)
			removeOption(code);
		else
			setOption(new DHCPOption(code, buf));
	}

	public void setOption(DHCPOption opt) {
		if (opt != null)
			if (opt.getValueFast() == null)
				removeOption(opt.getCode());
			else
				this.options.put(Byte.valueOf(opt.getCode()), opt);
	}

	public void setOptions(DHCPOption[] opts) {
		if (opts != null)
			for (DHCPOption opt : opts)
				setOption(opt);
	}

	public void setOptions(Collection<DHCPOption> opts) {
		if (opts != null)
			for (DHCPOption opt : opts)
				setOption(opt);
	}

	public void removeOption(byte opt) {
		this.options.remove(Byte.valueOf(opt));
	}

	public void removeAllOptions() {
		this.options.clear();
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public void setAddress(InetAddress address) {
		if (address == null) {
			this.address = null;
		} else {
			if (!(address instanceof Inet4Address)) {
				throw new IllegalArgumentException("only IPv4 addresses accepted");
			}
			this.address = address;
		}
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetSocketAddress getAddrPort() {
		return new InetSocketAddress(this.address, this.port);
	}

	public void setAddrPort(InetSocketAddress addrPort) {
		if (addrPort == null) {
			setAddress(null);
			setPort(0);
		} else {
			setAddress(addrPort.getAddress());
			setPort(addrPort.getPort());
		}
	}

	static String bytesToString(byte[] buf) {
		if (buf == null)
			return "";
		return bytesToString(buf, 0, buf.length);
	}

	static String bytesToString(byte[] buf, int src, int len) {
		if (buf == null)
			return "";
		if (src < 0) {
			len += src;
			src = 0;
		}
		if (len <= 0)
			return "";
		if (src >= buf.length)
			return "";
		if (src + len > buf.length)
			len = buf.length - src;

		for (int i = src; i < src + len; i++) {
			if (buf[i] == 0) {
				len = i - src;
				break;
			}
		}

		char[] chars = new char[len];

		for (int i = src; i < src + len; i++) {
			chars[(i - src)] = ((char) buf[i]);
		}
		return new String(chars);
	}

	static void appendHex(StringBuilder sbuf, byte b) {
		int i = b & 0xFF;
		sbuf.append(hex[((i & 0xF0) >> 4)]).append(hex[(i & 0xF)]);
	}

	static void appendHex(StringBuilder sbuf, byte[] buf, int src, int len) {
		if (buf == null)
			return;
		if (src < 0) {
			len += src;
			src = 0;
		}
		if ((len <= 0) || (src >= buf.length))
			return;
		if (src + len > buf.length)
			len = buf.length - src;

		for (int i = src; i < src + len; i++)
			appendHex(sbuf, buf[i]);
	}

	static void appendHex(StringBuilder sbuf, byte[] buf) {
		appendHex(sbuf, buf, 0, buf.length);
	}

	static String bytes2Hex(byte[] buf) {
		if (buf == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(buf.length * 2);
		appendHex(sb, buf);
		return sb.toString();
	}

	static byte[] hex2Bytes(String s) {
		if ((s.length() & 0x1) != 0) {
			throw new IllegalArgumentException("String length must be even: " + s.length());
		}

		byte[] buf = new byte[s.length() / 2];

		for (int index = 0; index < buf.length; index++) {
			int stringIndex = index << 1;
			buf[index] = ((byte) Integer.parseInt(s.substring(stringIndex, stringIndex + 2), 16));
		}
		return buf;
	}

	private static void appendHex(StringBuilder sbuf, int i) {
		appendHex(sbuf, (byte) ((i & 0xFF000000) >>> 24));
		appendHex(sbuf, (byte) ((i & 0xFF0000) >>> 16));
		appendHex(sbuf, (byte) ((i & 0xFF00) >>> 8));
		appendHex(sbuf, (byte) (i & 0xFF));
	}

	public static byte[] stringToBytes(String str) {
		if (str == null)
			return null;

		char[] chars = str.toCharArray();
		int len = chars.length;
		byte[] buf = new byte[len];

		for (int i = 0; i < len; i++) {
			buf[i] = ((byte) chars[i]);
		}
		return buf;
	}

	public static void appendHostAddress(StringBuilder sbuf, InetAddress addr) {
		if (addr == null) {
			throw new IllegalArgumentException("addr must not be null");
		}
		if (!(addr instanceof Inet4Address)) {
			throw new IllegalArgumentException("addr must be an instance of Inet4Address");
		}

		byte[] src = addr.getAddress();

		sbuf.append(src[0] & 0xFF).append('.').append(src[1] & 0xFF).append('.').append(src[2] & 0xFF).append('.').append(src[3] & 0xFF);
	}

	public static String getHostAddress(InetAddress addr) {
		StringBuilder sbuf = new StringBuilder(15);
		appendHostAddress(sbuf, addr);
		return sbuf.toString();
	}
}
