package com.fxz.DNSServer.Mina.Handler.IRecordImpl;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.TXTRecord;

import com.fxz.DNSServer.Mina.Handler.IRecordProcess;
import com.fxz.DNSServer.Utils.Params;

@Service
public class TXT implements IRecordProcess {
	Logger logger = Logger.getLogger(TXT.class);

	public void processReq(Message message, IoSession session) throws IOException {
		// TODO Auto-generated method stub
		Record record = message.getQuestion();
		Message outdata = (Message) message.clone();
		String host = record.getName().toString();
		host = host.substring(0, host.length() - 1);
		outdata.removeAllRecords(Section.ANSWER);
		Record answer = new TXTRecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), Params.getDnsConfig().getTxt());
		outdata.addRecord(answer, Section.ANSWER);
		byte[] buf = outdata.toWire();
		IoBuffer response = IoBuffer.wrap(buf);
		session.write(response);
		logger.info("TXTRecord->" + record.getName().toString());
		Params.getMqService().sendDnsLog(host, "TXT", session);
	}

}
