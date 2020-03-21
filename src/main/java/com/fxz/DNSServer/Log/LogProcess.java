package com.fxz.DNSServer.Log;

import java.util.Date;

import com.fxz.DNSServer.Utils.Params;

/**
 * @ClassName: LogProcess
 * @Description: 将查询记录发给日志处理队列，队列消费之负责将记录写入数据库并更新对应记录的查询次数，方便对域名和查询类型进行排名
 * @author: Administrator
 * @date: 2018年8月16日 上午9:34:50
 */
public class LogProcess {

	@SuppressWarnings("deprecation")
	public static void sendLogQue(String url, String type, String mac, String host, String hostname) {
		// www.baidu.com A 00-00-00-00-00 192.168.10.100 fuled-server Date
		Params.getMqService().sendMessage(Params.getLogQue(), url + "|" + type + "|" + mac + "|" + host + "|" + hostname + "|" + new Date().toLocaleString());
	}
}
