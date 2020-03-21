package com.fxz.DNSServer.DNS.Update;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

public class TestQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Record[] records = null;
			Lookup lookup = new Lookup("www.163.com", Type.TXT);
			lookup.setResolver(new SimpleResolver("114.114.114.114"));
			lookup.run();
			if (lookup.getResult() == Lookup.SUCCESSFUL) {
				records = lookup.getAnswers();
			} else {
				System.out.println("error");
				return;
			}
			for (int i = 0; i < records.length; i++) {
				
				System.out.println(records[i]+"  ip->"+records[i].rdataToString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
