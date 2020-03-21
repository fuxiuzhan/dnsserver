package com.fxz.DNSServer.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getSerialDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getSimpleDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static int getWeek() {
		Calendar clr = Calendar.getInstance();
		clr.setTime(new Date());
		return clr.get(Calendar.DAY_OF_WEEK);
	}
}
