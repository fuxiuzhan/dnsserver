package com.fxz.DNSServer.ActiveMq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MqListener implements MessageListener {

	public void onMessage(Message message) {
		TextMessage tm = (TextMessage) message;
		try {
			System.out.println("get Message->" + tm.getText());
			// do something ...
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
