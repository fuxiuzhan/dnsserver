package com.fxz.DNSServer.Mina.Handler.IRecordImpl;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;
import com.fxz.DNSServer.DNS.Update.DNSLookup;
import com.fxz.DNSServer.Dubbo.WrapDnsService;
import com.fxz.DNSServer.Mina.Handler.IRecordProcess;
import com.fxz.DNSServer.Utils.Params;
import sun.net.util.IPAddressUtil;

/**
 * @ClassName: A
 * @Description: DNS A记录，以A记录为例，其查询顺序为：Redis查询M管理记录若命中则返回，
 *               Redis查询A记录若命中则返回，Update查询若命中更新Redis返回， 若未命中读DB，若未命中返回默认值，
 * @author: Administrator
 * @date: 2018年8月14日 上午9:33:22
 */
@Service
public class A implements IRecordProcess {
	Logger logger = Logger.getLogger(A.class);

	public void processReq(Message message, IoSession session) throws IOException {
		// TODO Auto-generated method stub
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
			for (String ip : mip) {
				if (IPAddressUtil.isIPv4LiteralAddress(ip)) {
					InetAddress answerip = InetAddress.getByName(ip);
					Record answer = new ARecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), answerip);
					outdata.addRecord(answer, Section.ANSWER);
				}
			}
			byte[] buf = outdata.toWire();
			IoBuffer response = IoBuffer.wrap(buf);
			session.write(response);
			logger.info("MARecord->" + host);
			Params.getMqService().sendDnsLog(host, "A", session);
			return;
		}
		// 查询A记录是否存在缓存中
		String ipString = Params.getRedis().getKey("A" + host);
		if (ipString == null) {
			// 查询A记录缓存未命中，开始向目标Server更新
			Record[] records = new DNSLookup().Lookup(host, Type.A);
			if (records != null) {
				// 想目标Server更新成功
				StringBuilder sBuilder = new StringBuilder();
				for (Record record2 : records) {
					sBuilder.append(record2.rdataToString() + ",");
					InetAddress answerip = InetAddress.getByName(record2.rdataToString());
					Record answer = new ARecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), answerip);
					outdata.addRecord(answer, Section.ANSWER);
				}
				// 发送结果并更新缓存
				byte[] buf = outdata.toWire();
				IoBuffer response = IoBuffer.wrap(buf);
				session.write(response);
				Params.getRedis().setKey("A" + host, sBuilder.toString());
				// 向队列发送更新信息，更新数据库
				Params.getMqService().sendMessageUpdate("A|" + host + "|" + sBuilder.toString());
			} else {
				// 向目标Server更新失败，开始查询DB
				logger.error("ARecord Error");
				// read database
				ipString = WrapDnsService.querryDNS(host, "A");
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
					Params.getRedis().setKey("A" + host, ipString, Params.getDnsConfig().getTempLiveTime());
				} else {
					// 查询DB失败，返回默认值
					InetAddress answerip = InetAddress.getByName(Params.getDnsConfig().getDefaultTarget());
					Record answer = new ARecord(record.getName(), record.getDClass(), Params.getDnsConfig().getTtl(), answerip);
					outdata.addRecord(answer, Section.ANSWER);
					byte[] buf = outdata.toWire();
					IoBuffer response = IoBuffer.wrap(buf);
					session.write(response);
					Params.getRedis().setKey("A" + host, Params.getDnsConfig().getDefaultTarget(), Params.getDnsConfig().getTempLiveTime());
				}
			}
		} else {
			// 命中缓存
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
		}
		logger.info("ARecord->" + host);
		Params.getMqService().sendDnsLog(host, "A", session);
	}

}
