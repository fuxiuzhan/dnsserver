package com.fxz.DNSServer.DNS.Update;

import org.springframework.stereotype.Service;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import com.fxz.DNSServer.Utils.Params;

/**
 * @ClassName: DNSLookup
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年8月13日 下午2:46:55
 */
@Service
public class DNSLookup {
	public Record[] Lookup(String host, int Type) {
		for (String dns_server : Params.getDnsConfig().getParentDns().split(",")) {
			try {
				Lookup lookup = new Lookup(host, Type);
				lookup.setResolver(new SimpleResolver(dns_server));
				lookup.run();
				if (lookup.getResult() == Lookup.SUCCESSFUL) {
					return lookup.getAnswers();
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
