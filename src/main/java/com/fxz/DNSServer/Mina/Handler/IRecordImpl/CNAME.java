package com.fxz.DNSServer.Mina.Handler.IRecordImpl;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;
import com.fxz.DNSServer.DNS.Update.DNSLookup;
import com.fxz.DNSServer.Dubbo.WrapDnsService;
import com.fxz.DNSServer.Mina.Handler.IRecordProcess;
import com.fxz.DNSServer.Utils.Params;

import sun.net.util.IPAddressUtil;

@Service
public class CNAME implements IRecordProcess {
	Logger logger = Logger.getLogger(CNAME.class);

	public void processReq(Message message, IoSession session) throws IOException {
		Record record = message.getQuestion();
		Message outdata = (Message) message.clone();
		outdata.removeAllRecords(Section.ANSWER);
		String host = record.getName().toString();
		host = host.substring(0, host.length() - 1);
		// 查询A记录是否受到管控
		String mIps = null;
		if (Params.isManagerd()) {
			mIps = Params.getRedis().getKey("MA" + host);
		}
		if (mIps != null) {
			String[] mip = mIps.split(",");
			for (String host_s : mip) {
				Name name = Name.fromString(host_s, Name.root);
				Record answer = new CNAMERecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), name);
				outdata.addRecord(answer, Section.ANSWER);
			}
			byte[] buf = outdata.toWire();
			IoBuffer response = IoBuffer.wrap(buf);
			session.write(response);
			logger.info("MCNAMERecord->" + host);
			Params.getMqService().sendDnsLog(host, "CNAME", session);
			return;
		}
		// 查询A记录是否存在缓存中
		String ipString = Params.getRedis().getKey("CNAME" + host);
		if (ipString == null) {
			// 查询A记录缓存未命中，开始向目标Server更新
			Record[] records = new DNSLookup().Lookup(host, Type.CNAME);
			if (records != null) {
				// 想目标Server更新成功
				StringBuilder sBuilder = new StringBuilder();
				for (Record record2 : records) {
					sBuilder.append(record2.rdataToString() + ",");
					Name name = Name.fromString(record2.rdataToString(), Name.root);
					Record answer = new CNAMERecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), name);
					outdata.addRecord(answer, Section.ANSWER);
				}
				// 发送结果并更新缓存
				byte[] buf = outdata.toWire();
				IoBuffer response = IoBuffer.wrap(buf);
				session.write(response);
				Params.getRedis().setKey("CNAME" + host, sBuilder.toString());
				// 向队列发送更新信息，更新数据库
				Params.getMqService().sendMessageUpdate("CNAME|" + host + "|" + sBuilder.toString());
			} else {
				// 向目标Server更新失败，开始查询DB
				logger.error("NSRecord Error");
				// read database
				ipString = WrapDnsService.querryDNS(host, "CNAME");
				logger.info("Querry->" + ipString);
				if (ipString != null) {
					String[] ips = ipString.split(",");
					for (String ip : ips) {
						if (ip != null && IPAddressUtil.isIPv4LiteralAddress(ip)) {
							InetAddress answerip = InetAddress.getByName(ip);
							Record answer = new ARecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), answerip);
							outdata.addRecord(answer, Section.ANSWER);
						}
					}
					byte[] buf = outdata.toWire();
					IoBuffer response = IoBuffer.wrap(buf);
					session.write(response);
					Params.getRedis().setKey("CNAME" + host, ipString, Params.getDnsConfig().getTempLiveTime());
				} else {
					// 查询DB失败，返回默认值
					Name name = Name.fromString(Params.getDnsConfig().getServerName(), Name.root);
					Record answer = new CNAMERecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), name);
					outdata.addRecord(answer, Section.ANSWER);
					byte[] buf = outdata.toWire();
					IoBuffer response = IoBuffer.wrap(buf);
					session.write(response);
					Params.getRedis().setKey("CNAME" + host, Params.getDnsConfig().getServerName(), Params.getDnsConfig().getTempLiveTime());
				}
			}
		} else {
			// 命中缓存
			String[] ips = ipString.split(",");
			for (String host_s : ips) {
				Name name = Name.fromString(host_s, Name.root);
				Record answer = new CNAMERecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), name);
				outdata.addRecord(answer, Section.ANSWER);
				byte[] buf = outdata.toWire();
				IoBuffer response = IoBuffer.wrap(buf);
				session.write(response);
			}
		}
		logger.info("CNAMERecord->" + host);
		Params.getMqService().sendDnsLog(host, "CNAME", session);
	}

}
