package com.fxz.DNSServer.Mina.Handler.IRecordImpl;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Message;

import com.fxz.DNSServer.Mina.Handler.IRecordProcess;
@Service
public class NoneRecord implements IRecordProcess {

	public void processReq(Message message, IoSession session) throws IOException {
		// TODO Auto-generated method stub
		session.closeNow();
	}

}
