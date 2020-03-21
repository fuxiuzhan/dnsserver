package com.fxz.DNSServer.API;

import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestWrapper;

public class WriteReq extends WriteRequestWrapper {

	public WriteReq(WriteRequest parentRequest) {
		super(parentRequest);
		megString = super.getMessage().toString();
	}

	String megString;

	public void setMessage(String message) {
		megString = message;
	}

	@Override
	public Object getMessage() {
		// TODO Auto-generated method stub
		return megString;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return megString;
	}

}
