package com.fxz.DNSServer.Mybatis;

import org.springframework.stereotype.Service;

import com.fxz.DNSServer.Utils.Params;
import com.fxz.dns.dal.models.sysconfig;

/**
 * @ClassName: DnsConfig
 * @Description: dns服务器参数配置
 * @author: Administrator
 * @date: 2018年8月14日 下午7:10:29
 */
@Service
public class DnsConfig {
	private String serverName = "fuled.public.dns.com";
	private int ttl = 64;
	private int livetime = 2 * 60;
	private String txt = "v=spf1 a mx ~all \r\n admin fuxiuzhan@163.com ";
	private String parentDns = "114.114.114.114";
	private boolean enable = true;
	private String mailto = "fuxiuzhan@163.com";
	private String subject = "dnsserver running statstic";
	private String mailfrom = "Server@fuled.dns.com";
	private String mailTime = "00:00";
	private String defaultTarget = "192.168.10.128";
	private int tempLiveTime = 10;
	private String adminMail = "fuxiuzhan@163.com";

	public void wrap(sysconfig sysConfig) {
		parentDns = sysConfig.getParentDns();
		enable = sysConfig.getEnable();
		livetime = sysConfig.getAliveN();
		tempLiveTime = sysConfig.getAliveT();
		defaultTarget = sysConfig.getDefaultIp();
		adminMail = sysConfig.getMailAddr();
		Params.setSysconfig(sysConfig);
	}

	public String getAdminMail() {
		return adminMail;
	}

	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}

	public int getTempLiveTime() {
		return tempLiveTime;
	}

	public void setTempLiveTime(int tempLiveTime) {
		this.tempLiveTime = tempLiveTime;
	}

	public String getDefaultTarget() {
		return defaultTarget;
	}

	public void setDefaultTarget(String defaultTarget) {
		this.defaultTarget = defaultTarget;
	}

	public int getLivetime() {
		return livetime;
	}

	public void setLivetime(int livetime) {
		this.livetime = livetime;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getParentDns() {
		return parentDns;
	}

	public void setParentDns(String parentDns) {
		this.parentDns = parentDns;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getMailto() {
		return mailto;
	}

	public void setMailto(String mailto) {
		this.mailto = mailto;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailfrom() {
		return mailfrom;
	}

	public void setMailfrom(String mailfrom) {
		this.mailfrom = mailfrom;
	}

	public String getMailTime() {
		return mailTime;
	}

	public void setMailTime(String mailTime) {
		this.mailTime = mailTime;
	}

}
