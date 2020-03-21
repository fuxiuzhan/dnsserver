package com.fxz.DNSServer.Utils;

import com.fxz.DNSServer.SysMon.MonitorServiceImpl;

public class StaticsThread extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		int ttotal = 0;
		int mailTimer = 0;
		while (true) {
			// 提交更新信息，计算速度，检查是否需要发送统计邮件，检查是否需要重置统计数据（1s）
			ttotal = Statics.getCntTotal();
			try {
				Thread.sleep(1 * 1000);
				i++;
				mailTimer++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Params.getMaxSpeed() < (Statics.getCntTotal() - ttotal)) {
				Params.setMaxSpeed((Statics.getCntTotal() - ttotal));
				Params.setMaxSpeedTime(DateUtil.getSimpleDate());
			}
			Params.setSpeed((Statics.getCntTotal() - ttotal));
			if (i == 30) {
				// 检查是否需要发送统计邮件
				if (DateUtil.getSimpleDate().startsWith(Params.getSysconfig().getMailTime()) && mailTimer > 60) {
					// send mail
					try {
						Params.getMqService().sendMessage(Params.getMailQue(), StaticsHtmlBuilder.qMailString.replaceAll("\\$SysInfo", StaticsHtmlBuilder.getSysInfo(new MonitorServiceImpl().getMonitorInfoBean())).replaceAll("\\$IP_List", StaticsHtmlBuilder.getIPList()).replaceAll("\\$Record", StaticsHtmlBuilder.getDnsStatics()));
						mailTimer = 0;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (DateUtil.getSimpleDate().startsWith(Params.getSysconfig().getResetTime())) {
					// reset
					Statics.clear();
				}
			}
			if (i == 60) {
				// 60s 发送阶段统计信息
				Params.getMqService().sendMessage(Params.getStatics(), Statics.getRedisStatics() + "\r\n" + Statics.getStatics());
				i = 0;
			}

		}
	}
}
