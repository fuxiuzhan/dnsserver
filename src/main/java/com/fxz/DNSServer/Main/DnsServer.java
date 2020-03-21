package com.fxz.DNSServer.Main;

import javax.annotation.Resource;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import com.fxz.DNSServer.API.CtrlServer;
import com.fxz.DNSServer.ActiveMq.ProducerService;
import com.fxz.DNSServer.MAC.IP2MAC;
import com.fxz.DNSServer.Mail.SendMail;
import com.fxz.DNSServer.Mina.Handler.CommandFactory.RecordFactory;
import com.fxz.DNSServer.Mina.Listener.ServerListener;
import com.fxz.DNSServer.Mybatis.DnsConfig;
import com.fxz.DNSServer.Redis.Redis;
import com.fxz.DNSServer.Utils.Params;
import com.fxz.DNSServer.Utils.StaticsThread;
import com.fxz.dubbo.DubboService.IDNSService;

@Service
public class DnsServer {
	@Autowired
	Redis redis;
	@Autowired
	RecordFactory recordFactory;
	@Autowired
	ServerListener serverListener;
	@Autowired
	ProducerService mqService;
	@Autowired
	SendMail sendMail;
	@Autowired
	DnsConfig dnsConfig;
	@Resource(name = "dnsService")
	IDNSService dnsService;

	public void start() {
		System.err.println(dnsService.getSysConfig());
		dnsConfig.wrap(dnsService.getSysConfig());
		Params.setDnsService(dnsService);
		Params.setBlackList(dnsService.getBlackList());
		System.out.println("Dubbo ServiceProvider initlize .....ok");
		Params.setRedis(redis);
		System.out.println("Redis initlize ......ok");
		Params.setMqService(mqService);
		System.out.println("Mq initlize .....ok");
		recordFactory.initlize();
		Params.setRecordFactory(recordFactory);
		System.out.println("recordFactory inilize .....ok");
		Params.setSendMail(sendMail);
		System.out.println("Mail initlize .....ok");
		Params.setDnsConfig(dnsConfig);
		System.out.println("Dns Config initlize ..... ok");
		try {
			IP2MAC.startScanMac();
			System.out.println("Mac Scan initlize .....ok");
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Mac Scan Error.....");
		}
		new StaticsThread().start();
		System.out.println("Dns Statics initlize ....ok");
		serverListener.start();
		System.out.println("Server Started!");
		new CtrlServer().start();
		System.out.println("CtrlServer Started!");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure("config/log4j.properties");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml", "spring-redis.xml", "spring-mq.xml", "spring-dubbo.xml");
		context.start();
		DnsServer dnsServer = (DnsServer) context.getBean("dnsServer");
		dnsServer.start();
		synchronized (DnsServer.class) {
			while (true) {
				try {
					DnsServer.class.wait();
				} catch (Throwable e) {
				}
			}

		}
	}

}
