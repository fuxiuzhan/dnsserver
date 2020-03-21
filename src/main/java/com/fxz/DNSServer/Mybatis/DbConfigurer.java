package com.fxz.DNSServer.Mybatis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.fxz.DNSServer.Utils.Params;

public class DbConfigurer extends PropertyPlaceholderConfigurer {
	Logger log = Logger.getLogger(DbConfigurer.class);

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		// TODO Auto-generated method stub
		log.info("propertyName->" + propertyName + "  propertyValue->" + propertyValue);
		if (propertyName.equalsIgnoreCase("mq.log")) {
			Params.setLogQue(propertyValue);
		}
		if (propertyName.equalsIgnoreCase("mq.email")) {
			Params.setMailQue(propertyValue);
		}
		if (propertyName.equalsIgnoreCase("mq.statics")) {
			Params.setStatics(propertyValue);
		}
		return super.convertProperty(propertyName, propertyValue);
	}

}
