package com.fxz.DNSServer.ActiveMq;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import com.fxz.DNSServer.MAC.IP2MAC;
import com.fxz.DNSServer.Utils.DateUtil;
import com.fxz.DNSServer.Utils.Params;
import java.net.InetSocketAddress;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class ProducerService {

	Logger logger = Logger.getLogger(ProducerService.class);
	@Resource(name = "jmsTemplateUpdate")
	private JmsTemplate jmsTemplateUpdate;

	public void sendMessageUpdate(Destination destination, final String msg) {
		logger.info(Thread.currentThread().getName() + " QueName->" + destination.toString() + "  Mesg->" + msg);
		jmsTemplateUpdate.setTimeToLive(2 * 60 * 1000);
		jmsTemplateUpdate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
	}

	public void sendMessageUpdate(final String msg) {
		Params.getFixedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String destination = jmsTemplateUpdate.getDefaultDestinationName();
				logger.info(Thread.currentThread().getName() + " QueName->" + destination.toString() + "  Mesg->" + msg);
				jmsTemplateUpdate.setTimeToLive(2 * 60 * 1000);
				jmsTemplateUpdate.send(new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(msg);
					}
				});
			}
		});
	}

	public void sendDnsLog(final String querryContext, final String type, final IoSession session) {
		Params.getFixedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String mac = IP2MAC.getMAC(session);
				String host = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
				String hostname = IP2MAC.getHostName(host);
				Params.getRedis().setKey("E" + host, host, 60);
				final String meString = host + "|" + hostname + "|" + mac + "|" + type + "|" + querryContext + "|" + DateUtil.getStringDate();
				logger.info(Thread.currentThread().getName() + " QueName->" + Params.getLogQue() + "  Mesg->" + meString);
				jmsTemplateUpdate.setTimeToLive(2 * 60 * 1000);
				jmsTemplateUpdate.send(Params.getLogQue(), new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(meString);
					}
				});
			}
		});

	}

	public void sendMessage(final String quename, final String msg) {
		Params.getFixedThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				logger.info(Thread.currentThread().getName() + " QueName->" + quename + "  Mesg->" + msg);
				jmsTemplateUpdate.setTimeToLive(2 * 60 * 1000);
				jmsTemplateUpdate.send(quename, new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(msg);
					}
				});
			}
		});
	}
}