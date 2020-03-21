package com.fxz.DNSServer.API;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.fxz.DNSServer.Utils.Utils;

public class ApiUtilis {

	public Map<String, String> sendCommand(String username, String passwd, int type, String value, String ip, int port) {
		Map<String, String> resultMap = new HashMap<>();
		BaseMesage baseMesage = new BaseMesage();
		baseMesage.setPasswd(passwd);
		baseMesage.setUsername(username);
		baseMesage.setType(type);
		baseMesage.setValue(value);
		BufferedWriter writer = null;
		BufferedReader reader = null;
		Socket socket = null;
		try {
			String sendmessage = Utils.Byte2Hex(Utils.object2Json(baseMesage).getBytes());
			socket = new Socket(ip, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer.write(sendmessage + "\r\n");
			writer.flush();
			BaseMesage resultMessage = (BaseMesage) Utils.json2Object(new String(Utils.Hex2Byte(reader.readLine())), BaseMesage.class, null);
			resultMap.put("code", resultMessage.getResultCode());
			resultMap.put("data", resultMessage.getResult());
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "10");
			resultMap.put("data", "");
			return resultMap;
		} finally {
			try {
				socket.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}

