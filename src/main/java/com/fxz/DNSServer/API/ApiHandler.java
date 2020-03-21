/**
 * @Copyright © 2018 fuxiuzhan Fts Team All rights reserved.
 * @Package: com.fxz.DNSServer.API 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年12月17日 上午9:50:10 
 * 
 */
package com.fxz.DNSServer.API;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import com.fxz.DNSServer.MAC.IP2MAC;
import com.fxz.DNSServer.SysMon.MonitorServiceImpl;
import com.fxz.DNSServer.Utils.Params;
import com.fxz.DNSServer.Utils.Statics;

/**
 * @ClassName: ApiHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年12月17日 上午9:50:10
 */

public class ApiHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof BaseMesage) {
			BaseMesage baseMesage = (BaseMesage) message;
			switch (baseMesage.getType()) {
			case BaseMesage.CpuUsage:
				baseMesage.setResult(String.valueOf(new MonitorServiceImpl().getMonitorInfoBean().getCpuRatio()));
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.EnableBlackList:
				if (baseMesage.getValue().equals("true")) {
					baseMesage.setResultCode("00");
					session.write(baseMesage);
				}
				break;
			case BaseMesage.EnableSystem:
				Params.getDnsConfig().setEnable(baseMesage.getValue().equals("false") ? false : true);
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.MemUsage:
				baseMesage.setResult(String.valueOf(new MonitorServiceImpl().getMonitorInfoBean().getFreeMemory()));
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.ReloadBlackList:
				Params.setBlackList(Params.getDnsService().getBlackList());
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.ReloadConfig:
				Params.getDnsConfig().wrap(Params.getDnsService().getSysConfig());
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.ReloadStatics:
				Statics.clear();
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.StartScanMac:
				IP2MAC.startScanMac();
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.Statics:
				baseMesage.setResult(Statics.getStatics());
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.StopScanMac:
				IP2MAC.stop();
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			case BaseMesage.TPS:
				baseMesage.setResult(String.valueOf(Params.getSpeed()));
				baseMesage.setResultCode("00");
				session.write(baseMesage);
				break;
			default:
				break;
			}
		} else {
			session.close();
		}
	}

}
