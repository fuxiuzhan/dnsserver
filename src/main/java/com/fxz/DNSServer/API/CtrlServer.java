/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.DNSServer.RemoteCtrl 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月23日 下午7:46:23 
 * 
 */
package com.fxz.DNSServer.API;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * @ClassName: CtrlServer
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月23日 下午7:46:23
 */

public class CtrlServer extends Thread {
	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(32 * 1024);
		textLineCodecFactory.setEncoderMaxLineLength(32 * 1024);
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(textLineCodecFactory));
		acceptor.getFilterChain().addLast("threadpool", new ExecutorFilter(executorService));
		acceptor.getFilterChain().addLast("jsonfilter", new ApiFilter());
		acceptor.setHandler(new ApiHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60 * 10);
		try {
			acceptor.bind(new InetSocketAddress(2048));
			System.out.println("Listen Started");
		} catch (IOException e) {
			System.out.println("Bind Error!");
		}

	}
}
