/**
 * @Copyright © 2018 fuxiuzhan Fts Team All rights reserved.
 * @Package: com.fxz.DNSServer.API 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年12月17日 上午11:12:27 
 * 
 */
package com.fxz.DNSServer.API;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.fxz.DNSServer.Utils.Params;
import com.fxz.DNSServer.Utils.Utils;

/**
 * @ClassName: ApiFilter
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年12月17日 上午11:12:27
 */

public class ApiFilter extends IoFilterAdapter {

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
		session.close();
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		try {
			BaseMesage baseMesage = (BaseMesage) Utils.json2Object(new String(Utils.Hex2Byte(message.toString())), BaseMesage.class, null);
			if (baseMesage.getUsername().equals(Params.getSysconfig().getUserName()) && baseMesage.getPasswd().equals(Params.getSysconfig().getUserPasswd())) {
				nextFilter.messageReceived(session, baseMesage);
			} else {
				baseMesage.setResult("invalid username or passwd");
				baseMesage.setResultCode("10");
				session.write(baseMesage);
				session.closeOnFlush();
			}
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		if (writeRequest.getMessage() instanceof BaseMesage) {
			BaseMesage baseMesage = (BaseMesage) writeRequest.getMessage();
			String meString = Utils.Byte2Hex(Utils.object2Json(baseMesage).getBytes());
			WriteReq writeReq = new WriteReq(writeRequest);
			writeReq.setMessage(meString + "\r\n");
			super.filterWrite(nextFilter, session, writeReq);
		}
	}

}
