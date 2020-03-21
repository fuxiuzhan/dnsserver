package com.fxz.DNSServer.Mina.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import com.fxz.DNSServer.Mina.Filter.BlackListFilter;
import com.fxz.DNSServer.Mina.Filter.DerectFilter;
import com.fxz.DNSServer.Utils.Params;

public class TestNameServer {

	public static void main(String[] args) {
		new TestNameServer().startListener();
	}
	public void startListener() {
		final InetAddress answerip = (InetAddress)(new InetSocketAddress("127.0.0.1", 0).getAddress());
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		Executor threadPool = Executors.newCachedThreadPool();
		acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
		//acceptor.getFilterChain().addLast("logger", new com.fxz.DNSServer.Mina.Filter.LogFilter());
		//acceptor.getFilterChain().addLast("blacklist", new BlackListFilter());
		//acceptor.getFilterChain().addLast("derectall", new DerectFilter());
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3);
		acceptor.setHandler(new IoHandlerAdapter() {

			@Override
			public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
				// TODO Auto-generated method stub
				cause.printStackTrace();
			}

			@Override
			public void messageReceived(IoSession session, Object message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("IP->" + ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress());
				IoBuffer buffer = (IoBuffer) message;
				Message indata = new Message(buffer.array());
				Record record = indata.getQuestion();
				Message outdata = (Message) indata.clone();
				outdata.removeAllRecords(Section.ANSWER);
				String host = record.getName().toString();
				host = host.substring(0, host.length() - 1);
				System.out.println("host->" + host);
				Record answer = new ARecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), answerip);
				outdata.addRecord(answer, Section.ANSWER);
				byte[] buf = outdata.toWire();
				IoBuffer response = IoBuffer.wrap(buf);
				session.write(response);
			}

		});
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
