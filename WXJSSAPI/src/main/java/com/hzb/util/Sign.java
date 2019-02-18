package com.hzb.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author: houzhibo
 * @date: 2019年2月18日 上午9:36:08
 * @describe:
 */
public class Sign {

	public static String[] getSign(String jsapi_ticket) throws NoSuchAlgorithmException {
		String[] data = new String[10];
		Long timestamp = System.currentTimeMillis() / 1000;
		String noncestr = RandomStringUtils.randomAlphanumeric(16);

		String url = "http://hzb.nat300.top/demo.html";

		String shaStr = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(shaStr.getBytes());
		StringBuffer signature = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			signature.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		data[0] = timestamp.toString();
		data[1] = noncestr;
		data[2] = signature.toString();
		return data;
	}

	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		System.out.println(string1);
		System.out.println(jsapi_ticket);
		System.out.println(nonce_str);
		System.out.println(timestamp);
		System.out.println(url);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
			System.out.println(signature);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		ret.put("appId", "wxe7c0538d78f13ed9");

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

}
