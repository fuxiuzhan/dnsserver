package com.fxz.DNSServer.Mina.Listener;

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
import org.springframework.stereotype.Service;

import com.fxz.DNSServer.Mina.Handler.CtrlHandler;
import com.fxz.auth.config.AuthConfig;
import com.fxz.auth.filters.AuthFilter;

@Service
public class CtrlListener extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AuthConfig config = new AuthConfig();
		config.setDigest("md5");
		config.setNonSymEncryt("rsa");
		config.setSymEncrypt("aes");
		config.setPort(3162);
		config.setIp("127.0.0.1");
		config.setNonsymBlockSize(512);
		ExecutorService executorService = Executors.newCachedThreadPool();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(32 * 1024);
		textLineCodecFactory.setEncoderMaxLineLength(32 * 1024);
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(textLineCodecFactory));
		acceptor.getFilterChain().addLast("threadpool", new ExecutorFilter(executorService));
		acceptor.getFilterChain().addLast("auth", new AuthFilter(config));
		acceptor.setHandler(new CtrlHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10 * 60);
		try {
			acceptor.bind(new InetSocketAddress(config.getIp(), config.getPort()));
			System.out.println("Listen Started");
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Bind Error!");
			e.printStackTrace();
		}

	}
}
