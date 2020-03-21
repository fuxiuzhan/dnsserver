package com.fxz.DNSServer.Utils;

import java.util.Date;
import java.util.Set;

import com.fxz.DNSServer.MAC.IP2MAC;

public class Statics {
	private static int cntA = 0;
	private static int cntAAAA = 0;
	private static int cntCNAME = 0;
	private static int cntMX = 0;
	private static int cntNS = 0;
	private static int cntPTR = 0;
	private static int cntSOA = 0;
	private static int cntTXT = 0;
	private static int cntAny = 0;
	private static Date maxPoint = new Date();
	private static int maxSpeed = 0;

	public static void clear() {
		cntA = 0;
		cntAAAA = 0;
		cntCNAME = 0;
		cntMX = 0;
		cntNS = 0;
		cntPTR = 0;
		cntSOA = 0;
		cntTXT = 0;
		cntAny = 0;
		maxSpeed = 0;
		Params.setMaxSpeed(0);
	}

	public static int getCntA() {
		return cntA;
	}

	public static void addCntA() {
		Statics.cntA++;
	}

	public static int getCntAAAA() {
		return cntAAAA;
	}

	public static void addCntAAAA() {
		Statics.cntAAAA++;
	}

	public static int getCntCNAME() {
		return cntCNAME;
	}

	public static void addCntCNAME() {
		Statics.cntCNAME++;
	}

	public static int getCntMX() {
		return cntMX;
	}

	public static void addCntMX() {
		Statics.cntMX++;
	}

	public static int getCntNS() {
		return cntNS;
	}

	public static void addCntNS() {
		Statics.cntNS++;
	}

	public static int getCntPTR() {
		return cntPTR;
	}

	public static void addCntPTR() {
		Statics.cntPTR++;
	}

	public static int getCntSOA() {
		return cntSOA;
	}

	public static void addCntSOA() {
		Statics.cntSOA++;
	}

	public static int getCntTXT() {
		return cntTXT;
	}

	public static void addCntTXT() {
		Statics.cntTXT++;
	}

	public static int getCntAny() {
		return cntAny;
	}

	public static void addCntAny() {
		Statics.cntAny++;
	}

	public static Date getMaxPoint() {
		return maxPoint;
	}

	public static void setMaxPoint(Date maxPoint) {
		Statics.maxPoint = maxPoint;
	}

	public static int getMaxSpeed() {
		return maxSpeed;
	}

	public static void setMaxSpeed(int maxSpeed) {
		Statics.maxSpeed = maxSpeed;
	}

	public static int getCntTotal() {
		return cntA + cntAAAA + cntAny + cntCNAME + cntMX + cntNS + cntPTR + cntSOA;
	}

	public static String getStatics() {
		return "Total->" + getCntTotal() + " A->" + getCntA() + " AAAA->" + getCntAAAA() + " Any->" + getCntAny() + " CNAME->" + getCntCNAME() + " MX->" + getCntMX() + " NS->" + getCntNS() + " PTR->" + getCntPTR() + " SOA->" + getCntSOA() + " TXT->" + getCntTXT() + "  Speed->" + Params.getSpeed() + " q/s" + " maxSpeed->" + Params.getMaxSpeed() + " q/s" + " maxSpeedTime->"
				+ Params.getMaxSpeedTime();
	}

	public static String getRedisStatics() {
		Set<String> ipSet = Params.getRedis().keys("E*");
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\r\n");
		for (String string : ipSet) {
			sBuilder.append("IP->" + string.replaceAll("E", "") + " HostName->" + IP2MAC.getHostName(string.replaceAll("E", "")) + " MAC->" + IP2MAC.getMAC(string.replaceAll("E", "")) + "\r\n");
		}
		return sBuilder.toString();
	}
}
