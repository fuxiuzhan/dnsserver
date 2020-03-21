package com.fxz.DNSServer.Mina.Handler;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.xbill.DNS.Message;

/** 
 * @ClassName: RecordProcess 
 * @Description: dns请求处理接口
 * @author: Administrator
 * @date: 2018年8月13日 上午10:28:27  
 */
public interface IRecordProcess {
	public void processReq(Message message, IoSession session) throws IOException;
}
