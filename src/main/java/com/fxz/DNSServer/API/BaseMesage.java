/**
 * @Copyright © 2018 fuxiuzhan Fts Team All rights reserved.
 * @Package: com.fxz.DNSServer.API 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年12月17日 上午9:37:15 
 * 
 */
package com.fxz.DNSServer.API;

import java.io.Serializable;

/**
 * @ClassName: BaseMesage
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年12月17日 上午9:37:15
 */

public class BaseMesage implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	public final static int TPS = 0;// 获取tps
	public final static int Statics = 1;// 获取统计数据
	public final static int ReloadConfig = 2;// 刷新设置
	public final static int ReloadStatics = 3;// 清空统计
	public final static int StopScanMac = 4;// 停止mac扫描
	public final static int StartScanMac = 5;// 开启mac扫描
	public final static int CpuUsage = 6;// 获取cpu使用率
	public final static int MemUsage = 7;// 获取内存使用率
	public final static int ReloadBlackList = 8;// 刷新黑名单
	public final static int EnableBlackList = 9;// 启停黑名单
	public final static int EnableSystem = 10;// 启停系统
	public final static int EnableUpdate = 11;// 是否只做缓存
	private int type;
	private String value;
	private String result;
	private String resultCode;
	private String username;
	private String passwd;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

}
