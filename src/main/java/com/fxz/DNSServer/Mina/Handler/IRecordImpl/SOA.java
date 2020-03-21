package com.fxz.DNSServer.Mina.Handler.IRecordImpl;

import java.io.IOException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.Section;
import com.fxz.DNSServer.Mina.Handler.IRecordProcess;
import com.fxz.DNSServer.Utils.Params;

@Service
public class SOA implements IRecordProcess {
	public void processReq(Message message, IoSession session) throws IOException {
		// TODO Auto-generated method stub
		Record record = message.getQuestion();
		Message outdata = (Message) message.clone();
		String host = record.getName().toString();
		host = host.substring(0, host.length() - 1);
		outdata.removeAllRecords(Section.ANSWER);
		SOARecord soarecord = new SOARecord(record.getName(), record.getDClass(), 64, Name.fromString(Params.getDnsConfig().getServerName(), Name.root), Name.fromString(Params.getDnsConfig().getAdminMail(), Name.root), 1800L, 1800L, 900L, 606800L, 86400L);
		outdata.addRecord(soarecord, Section.ANSWER);
		byte[] buf = outdata.toWire();
		IoBuffer response = IoBuffer.wrap(buf);
		session.write(response);
		System.out.println("SOARecord->" + record.getName().toString());
		Params.getMqService().sendDnsLog(host, "SOA", session);
	}

}
