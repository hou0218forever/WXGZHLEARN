package com.hzb.util;

import net.sf.json.JSONObject;

/**
 * @author: houzhibo
 * @date: 2019年2月18日 上午9:35:52
 * @describe:
 */
public class JsapiTicketUtil {
	
	public static String getJSApiTicket() {
		// 获取token
		String acess_token = AccessTokenUtil.getAccess_Token();

		String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + acess_token
				+ "&type=jsapi";
		JSONObject backData = HttpUtil.doGetstr(urlStr);
		String ticket = backData.get("ticket").toString();
		return ticket;

	}
	public static void main(String[] args) {
		System.out.println(getJSApiTicket());
	}

}
