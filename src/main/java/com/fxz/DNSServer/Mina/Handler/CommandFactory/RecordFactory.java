package com.fxz.DNSServer.Mina.Handler.CommandFactory;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fxz.DNSServer.Mina.Handler.IRecordProcess;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.A;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.AAAA;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.Any;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.CNAME;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.MX;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.NS;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.NoneRecord;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.PTR;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.SOA;
import com.fxz.DNSServer.Mina.Handler.IRecordImpl.TXT;
import com.fxz.DNSServer.Utils.Statics;

/**
 * @ClassName: RecordFactory
 * @Description: 请求处理工厂
 * @author: Administrator
 * @date: 2018年8月13日 上午10:32:04
 */
@Service
public class RecordFactory {
	@Autowired
	A a;
	@Autowired
	AAAA aaaa;
	@Autowired
	NS ns;
	@Autowired
	CNAME cname;
	@Autowired
	PTR ptr;
	@Autowired
	MX mx;
	@Autowired
	SOA soa;
	@Autowired
	Any any;
	@Autowired
	NoneRecord noneRecord;
	@Autowired
	TXT txt;
	private HashMap<String, IRecordProcess> recordMap = new HashMap<String, IRecordProcess>();

	public void initlize() {
		recordMap.put("A", a);
		recordMap.put("AAAA", aaaa);
		recordMap.put("NS", ns);
		recordMap.put("CNAME", cname);
		recordMap.put("PTR", ptr);
		recordMap.put("MX", mx);
		recordMap.put("SOA", soa);
		recordMap.put("Any", any);
		recordMap.put("TXT", txt);
		recordMap.put("None", noneRecord);
		System.out.println("factory elms initlize ok...");
	}

	private IRecordProcess getRecord(String type) {
		return recordMap.get(type);

	}

	public IRecordProcess getRecord(int type) {
		switch (type) {
		case 1:
			Statics.addCntA();
			return getRecord("A");
		case 2:
			Statics.addCntNS();
			return getRecord("NS");
		case 5:
			Statics.addCntCNAME();
			return getRecord("CNAME");
		case 6:
			Statics.addCntSOA();
			return getRecord("SOA");
		case 12:
			Statics.addCntPTR();
			return getRecord("PTR");
		case 15:
			Statics.addCntMX();
			return getRecord("MX");
		case 16:
			Statics.addCntTXT();
			return getRecord("TXT");
		case 28:
			Statics.addCntAAAA();
			return getRecord("AAAA");
		case 255:
			Statics.addCntAny();
			return getRecord("Any");
		default:
			return getRecord("A");
		}
	}

	public void setRecord(String type, IRecordProcess recodprocessor) throws IOException {
		if (recordMap.containsKey(type)) {
			throw new IOException("Type->" + type + " has exist!");
		}
		recordMap.put(type, recodprocessor);
	}

}
