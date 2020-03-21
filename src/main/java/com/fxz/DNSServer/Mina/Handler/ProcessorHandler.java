package com.fxz.DNSServer.Mina.Handler;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.Message;

import com.fxz.DNSServer.Mina.Handler.CommandFactory.RecordFactory;
import com.fxz.DNSServer.Utils.Params;

/**
 * @ClassName: ProcessorHandler
 * @Description: mina Handler ，命令模式，与工厂结合完成请求路由与处理
 * @author: Administrator
 * @date: 2018年8月13日 上午10:59:54
 */
public class ProcessorHandler extends IoHandlerAdapter {

	@Autowired
	RecordFactory recordFactory;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof IoBuffer) {
			System.out.println("IP->" + ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress());
			IoBuffer buffer = (IoBuffer) message;
			Message indata = new Message(buffer.array());
			Params.getRecordFactory().getRecord(indata.getQuestion().getType()).processReq(indata, session);
			session.closeNow();
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
		session.closeOnFlush();
	}

}
