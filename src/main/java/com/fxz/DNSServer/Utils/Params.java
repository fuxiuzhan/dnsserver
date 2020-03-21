package com.fxz.DNSServer.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fxz.DNSServer.ActiveMq.ProducerService;
import com.fxz.DNSServer.Mail.SendMail;
import com.fxz.DNSServer.Mina.Handler.CommandFactory.RecordFactory;
import com.fxz.DNSServer.Mybatis.DnsConfig;
import com.fxz.DNSServer.Redis.Redis;
import com.fxz.dns.dal.models.sysconfig;
import com.fxz.dubbo.DubboService.IDNSService;

public class Params {
	private static RecordFactory recordFactory;
	private static Redis redis;
	private static ProducerService mqService;
	private static SendMail sendMail;
	private static DnsConfig dnsConfig;
	private static String logQue = "dnslog";
	private static String mailQue = "qmail";
	private static String statics = "dns_statics";
	private static IDNSService dnsService;
	private static sysconfig sysconfig;
	private static int speed=0;
	private static int maxSpeed=0;
	private static String maxSpeedTime=DateUtil.getStringDate();
	private static List<String> blackList;
	private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
	private static boolean managerd=false;
	//ctrl swith
	

	
	
	public static String getMaxSpeedTime() {
		return maxSpeedTime;
	}


	public static void setMaxSpeedTime(String maxSpeedTime) {
		Params.maxSpeedTime = maxSpeedTime;
	}

	public static boolean isManagerd() {
		return managerd;
	}

	public static void setManagerd(boolean managerd) {
		Params.managerd = managerd;
	}

	public static ExecutorService getFixedThreadPool() {
		return fixedThreadPool;
	}

	public static List<String> getBlackList() {
		return blackList;
	}

	public static void setBlackList(List<String> blackList) {
		Params.blackList = blackList;
	}

	public static int getMaxSpeed() {
		return maxSpeed;
	}

	public static void setMaxSpeed(int maxSpeed) {
		Params.maxSpeed = maxSpeed;
	}

	public static int getSpeed() {
		return speed;
	}

	public static void setSpeed(int speed) {
		Params.speed = speed;
	}

	public static sysconfig getSysconfig() {
		return sysconfig;
	}

	public static void setSysconfig(sysconfig sysconfig) {
		Params.sysconfig = sysconfig;
	}

	public static IDNSService getDnsService() {
		return dnsService;
	}

	public static void setDnsService(IDNSService dnsService) {
		Params.dnsService = dnsService;
	}

	public static String getStatics() {
		return statics;
	}

	public static void setStatics(String statics) {
		Params.statics = statics;
	}

	public static String getLogQue() {
		return logQue;
	}

	public static void setLogQue(String logQue) {
		Params.logQue = logQue;
	}

	public static String getMailQue() {
		return mailQue;
	}

	public static void setMailQue(String mailQue) {
		Params.mailQue = mailQue;
	}

	public static DnsConfig getDnsConfig() {
		return dnsConfig;
	}

	public static void setDnsConfig(DnsConfig dnsConfig) {
		Params.dnsConfig = dnsConfig;
	}

	public static SendMail getSendMail() {
		return sendMail;
	}

	public static void setSendMail(SendMail sendMail) {
		Params.sendMail = sendMail;
	}

	public static ProducerService getMqService() {
		return mqService;
	}

	public static void setMqService(ProducerService mqService) {
		Params.mqService = mqService;
	}

	public static RecordFactory getRecordFactory() {
		return recordFactory;
	}

	public static void setRecordFactory(RecordFactory recordFactory) {
		Params.recordFactory = recordFactory;
	}

	public static Redis getRedis() {
		return redis;
	}

	public static void setRedis(Redis redis) {
		Params.redis = redis;
	}

}
