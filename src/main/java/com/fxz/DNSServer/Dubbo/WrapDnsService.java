package com.fxz.DNSServer.Dubbo;

import com.fxz.DNSServer.Utils.Params;

public class WrapDnsService {

	public static String querryDNS(String key, String type) {
		String rez = Params.getDnsService().querryDNS(key, type);
		if (rez.equalsIgnoreCase("N/A")) {
			return null;
		}
		return rez;
	}

}
