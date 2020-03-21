package com.fxz.DNSServer.Mina.Handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class CtrlHandler extends IoHandlerAdapter {

	Logger logger = Logger.getLogger(CtrlHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		/*
		 * switchs 实现命令模式，主要实现查看列表（最近查询客户端，扫描到的主机信息） 设置参数（邮件，时间，默认地址等） 刷新缓存等
		 */
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}

}
