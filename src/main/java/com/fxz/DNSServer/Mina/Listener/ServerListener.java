package com.fxz.DNSServer.Mina.Listener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.springframework.stereotype.Service;
import com.fxz.DNSServer.Mina.Filter.BlackListFilter;
import com.fxz.DNSServer.Mina.Filter.DerectFilter;
import com.fxz.DNSServer.Mina.Handler.ProcessorHandler;

/**
 * @ClassName: ServerListener
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年8月13日 上午10:21:09
 */
@Service
public class ServerListener extends Thread {
	@Override
	public void run() {
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		Executor threadPool = Executors.newCachedThreadPool();
		acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
		acceptor.getFilterChain().addLast("logger", new com.fxz.DNSServer.Mina.Filter.LogFilter());
		acceptor.getFilterChain().addLast("blacklist", new BlackListFilter());
		acceptor.getFilterChain().addLast("derectall", new DerectFilter());
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3);
		acceptor.setHandler(new ProcessorHandler());
		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReadBufferSize(4096);
		dcfg.setReceiveBufferSize(1024);
		dcfg.setSendBufferSize(1024);
		dcfg.setReuseAddress(true);
		try {
			acceptor.bind(new InetSocketAddress(53));
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(-1);
		}

	}
}
