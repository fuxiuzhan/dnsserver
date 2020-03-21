package com.fxz.DNSServer.Mina.Filter;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import com.fxz.DNSServer.Utils.Params;

public class BlackListFilter extends IoFilterAdapter {
	Logger logger = Logger.getLogger(BlackListFilter.class);

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		// 根据IP或者MAC地址，适配黑名单
		String ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		if (Params.getBlackList() != null && Params.getBlackList().size() > 0 && Params.getBlackList().contains(ip)) {
			logger.warn("IP->" + ip + " in IPBlackList session closeing....");
			session.closeNow();
		} else {
			super.messageReceived(nextFilter, session, message);
		}
	}

}
