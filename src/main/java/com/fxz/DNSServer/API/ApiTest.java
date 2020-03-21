/**
 * @Copyright © 2018 fuxiuzhan Fts Team All rights reserved.
 * @Package: com.fxz.DNSServer.API 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年12月17日 上午11:27:08 
 * 
 */
package com.fxz.DNSServer.API;

import java.util.Map;

/**
 * @ClassName: ApiTest
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年12月17日 上午11:27:08
 */

public class ApiTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApiUtilis api = new ApiUtilis();
		Map<String, String> resultMap = api.sendCommand("fuled", "fuled", BaseMesage.Statics, "", "127.0.0.1", 2048);
		System.out.println(resultMap);
	}

}
