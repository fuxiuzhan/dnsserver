package com.fxz.DNSServer.Mina.Filter;

import java.net.InetAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;

import com.fxz.DNSServer.Utils.Params;

public class DerectFilter extends IoFilterAdapter {

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		if (Params.getDnsConfig().isEnable()) {
			super.messageReceived(nextFilter, session, message);
		} else {
			IoBuffer buffer = (IoBuffer) message;
			Message indata = new Message(buffer.array());
			Record question = indata.getQuestion();
			Message outdata = (Message) indata.clone();
			Record answer = new ARecord(question.getName(), question.getDClass(), 64, InetAddress.getByName(Params.getDnsConfig().getDefaultTarget()));
			outdata.addRecord(answer, Section.ANSWER);
			byte[] buf = outdata.toWire();
			IoBuffer response = IoBuffer.wrap(buf);
			session.write(response);
			session.closeNow();
			System.out.println("DerectAll Query,Derect QueryName=" + question.getName().toString() + " to DestinationAddress=" + Params.getDnsConfig().getDefaultTarget());
		}
	}
}
