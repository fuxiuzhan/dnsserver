package com.fxz.DNSServer.Utils;

import java.util.Set;
import com.fxz.DNSServer.MAC.IP2MAC;
import com.fxz.DNSServer.SysMon.MonitorInfo;

/**
 * @ClassName: StaticsHtmlBuilder
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年8月23日 上午8:46:18
 */
public class StaticsHtmlBuilder {
	public static String qMailString = "<section class=\"article135\" style=\"background-repeat:repeat;background-position:left top;padding:10px 10px;background-size:auto auto;background-image:url(http://image2.135editor.com/cache/remote/aHR0cHM6Ly9tbWJpei5xbG9nby5jbi9tbWJpel9qcGcvWVV5WjdBT0wzb2xsWWlhaDVIWkd6aEgyRVdpYXI5STA1WlpVTXppYWJQSHV1UUVpYjN5dXFIMThpYjJ5elpnRnRqUUZuVFBSQ0s5NjFXYk12R2p0WXdLUWljbHcvMD93eF9mbXQ9anBlZw==?imageView2/2/w/200);\">\r\n"
			+ "    <p>\r\n" + "        DNS_System_Status\r\n" + "    </p>\r\n" + "    <hr/>\r\n" + "    <p>\r\n" + "        <br/>\r\n" + "    </p>\r\n" + "    <p>\r\n" + "        SystemInfo(Estimate)\r\n" + "    </p>\r\n" + "    <table>\r\n" + "        <tbody>\r\n" + "            <tr class=\"firstRow\">\r\n" + "                <td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n"
			+ "                    Items\r\n" + "                </td>\r\n" + "                <td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Value\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "           $SysInfo\r\n" + "        </tbody>\r\n" + "    </table>\r\n" + "    <hr/>\r\n" + "    <p>\r\n" + "        <br/>\r\n"
			+ "    </p>\r\n" + "    <p>\r\n" + "        Client Infos<br/>\r\n" + "    </p>\r\n" + "    <table>\r\n" + "        <tbody>\r\n" + "            <tr class=\"firstRow\">\r\n" + "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    ID\r\n" + "                </td>\r\n"
			+ "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    IP\r\n" + "                </td>\r\n" + "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    MAC\r\n" + "                </td>\r\n"
			+ "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Host\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "            $IP_List\r\n" + "        </tbody>\r\n" + "    </table>\r\n" + "    <p>\r\n" + "        <br/>\r\n" + "    </p>\r\n" + "    <hr/>\r\n" + "    <p>\r\n" + "        <br/>\r\n" + "    </p>\r\n"
			+ "    <p>\r\n" + "        DNS_Status\r\n" + "    </p>\r\n" + "    <table>\r\n" + "        <tbody>\r\n" + "            <tr class=\"firstRow\">\r\n" + "                <td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Type\r\n" + "                </td>\r\n"
			+ "                <td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Count\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "            $Record\r\n" + "        </tbody>\r\n" + "    </table>\r\n" + "    <hr/>\r\n" + "    <p>\r\n" + "        <br/>\r\n" + "    </p>\r\n" + "    <p>\r\n" + "        Top(100)&nbsp;\r\n"
			+ "    </p>\r\n" + "    <table>\r\n" + "        <tbody>\r\n" + "            <tr class=\"firstRow\">\r\n" + "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    ID\r\n" + "                </td>\r\n" + "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Host\r\n"
			+ "                </td>\r\n" + "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Type\r\n" + "                </td>\r\n" + "                <td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "                    Count\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "            $Top\r\n"
			+ "        </tbody>\r\n" + "    </table>\r\n" + "    <hr/>\r\n" + "    <p style=\"text-align: center;\">\r\n" + "        <br/>\r\n" + "    </p>\r\n" + "    <p style=\"text-align: center;\">\r\n" + "        fuxiuzhan@163.com\r\n" + "    </p>\r\n" + "    <p>\r\n" + "        <br/>\r\n" + "    </p>\r\n" + "</section>";

	public static String getSysInfo(MonitorInfo Sysinfo) {
		// <td width="186" valign="top" style="word-break: break-all;"> <span
		// style="font-size: 14px;">Mem</span> </td>
		String listhtml = " <tr><td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + " <span style=\"font-size: 14px;\">item</span>\r\n" + "</td>\r\n" + "<td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "<span style=\"font-size: 14px;\">value</span>\r\n" + "</td></tr>";
		/*
		 * cpu占有率=0.0 可使用内存=123904 剩余内存=121937 最大可使用内存=1831424 操作系统=Windows 7
		 * 总的物理内存=8233308kb 剩余的物理内存=121937kb 已使用的物理内存=5600632kb 线程总数=5kb
		 */
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(getReplace(listhtml, "item", "OS", "value", Sysinfo.getOsName()));
		sBuilder.append(getReplace(listhtml, "item", "CPU Usage", "value", String.valueOf(Sysinfo.getCpuRatio() + " %")));
		sBuilder.append(getReplace(listhtml, "item", "Used Mem", "value", String.valueOf(Sysinfo.getUsedMemory() / 1024) + " MB"));
		sBuilder.append(getReplace(listhtml, "item", "Free Mem", "value", String.valueOf(Sysinfo.getFreeMemory() / 1024) + " MB"));
		sBuilder.append(getReplace(listhtml, "item", "Total Mem", "value", String.valueOf(Sysinfo.getTotalMemorySize() / 1024) + " MB"));
		sBuilder.append(getReplace(listhtml, "item", "Total Threads", "value", String.valueOf(Sysinfo.getTotalThread() * 1024)));
		return sBuilder.toString();
	}

	private static String getReplace(String context, String item, String itemv, String value, String valuev) {
		String moudle = new String(context);
		moudle = moudle.replaceAll(item, itemv);
		moudle = moudle.replaceAll(value, valuev);
		return moudle;
	}

	private static String getReplace(String id, String idv, String context, String item, String itemv, String value, String valuev) {
		String moudle = new String(context);
		moudle = moudle.replaceAll(id, idv);
		moudle = moudle.replaceAll(item, itemv);
		moudle = moudle.replaceAll(value, valuev);
		return moudle;
	}

	/**
	 * @Title: getIPList @Description: TODO @param @param
	 *         List<IP|MAC|HostName> @param @return @return String @throws
	 */
	public static String getIPList() {
		String listhtml = "<tr>\r\n" + "<td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "<span style=\"font-size: 12px;\">index</span>\r\n" + "</td>\r\n" + "<td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "<span style=\"font-size: 12px;\">ip</span>\r\n" + "</td>\r\n" + "<td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n"
				+ "<span style=\"font-size: 12px;\">mac</span>\r\n" + "</td>\r\n" + "<td width=\"82\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + "<span style=\"font-size: 12px;\">hostname</span>\r\n" + "</td>\r\n" + "</tr>";
		StringBuilder sBuilder = new StringBuilder();
		Set<String> ipSet = Params.getRedis().keys("E*");
		int i = 1;
		for (String string : ipSet) {
			String strtemp = getReplace("index", String.valueOf(i++), listhtml, "item", "item", "ip", string.replaceAll("E", ""));
			sBuilder.append(getReplace(strtemp, "mac", IP2MAC.getMAC(string.replaceAll("E", "")), "hostname", IP2MAC.getHostName(string.replaceAll("E", ""))));
		}
		return sBuilder.toString();
	}

	public static String getDnsStatics() {
		String listhtml = "<tr>\r\n" + "<td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + " <span style=\"font-size: 14px;\">item</span>\r\n" + "</td>\r\n" + "<td width=\"186\" valign=\"top\" style=\"word-break: break-all;\">\r\n" + " <span style=\"font-size: 14px;\">value</span>\r\n" + "</td>\r\n" + "</tr>";
		// TYPE Cnt
		// Total Cnt
		// Speed
		// MaxSpeed
		// MaxPoint
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(getReplace(listhtml, "item", "A", "value", String.valueOf(Statics.getCntA())));
		sBuilder.append(getReplace(listhtml, "item", "AAAA", "value", String.valueOf(Statics.getCntAAAA())));
		sBuilder.append(getReplace(listhtml, "item", "PTR", "value", String.valueOf(Statics.getCntPTR())));
		sBuilder.append(getReplace(listhtml, "item", "CNAME", "value", String.valueOf(Statics.getCntCNAME())));
		sBuilder.append(getReplace(listhtml, "item", "MX", "value", String.valueOf(Statics.getCntMX())));
		sBuilder.append(getReplace(listhtml, "item", "NS", "value", String.valueOf(Statics.getCntNS())));
		sBuilder.append(getReplace(listhtml, "item", "SOA", "value", String.valueOf(Statics.getCntSOA())));
		sBuilder.append(getReplace(listhtml, "item", "TXT", "value", String.valueOf(Statics.getCntTXT())));
		sBuilder.append(getReplace(listhtml, "item", "Total", "value", String.valueOf(Statics.getCntTotal())));
		sBuilder.append(getReplace(listhtml, "item", "MaxSpeed", "value", String.valueOf(Params.getMaxSpeed()) + " q/s"));
		sBuilder.append(getReplace(listhtml, "item", "TPoint", "value", String.valueOf(Params.getMaxSpeedTime())));
		return sBuilder.toString();
	}
}
