package com.hzb.util;

import com.hzb.bean.AccessToken;
import com.hzb.redis.RedisUtil;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * @author: houzhibo
 * @date: 2019年2月18日 上午9:35:52
 * @describe:
 */
public class AccessTokenUtil {

	/**
	 * 开发者id
	 */
	private static final String APPID = "wxe7c0538d78f13ed9";
	/**
	 * 开发者秘钥
	 */
	private static final String APPSECRET = "fff547bfbb9b4f1362ea1325729a4929";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?"
			+ "grant_type=client_credential&appid=APPID&secret=APPSECRET";

	public static AccessToken getAccessToken() {
		System.out.println("从接口中获取");
		Jedis jedis = RedisUtil.getJedis();
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject json = HttpUtil.doGetstr(url);
		if (json != null) {
			token.setAccess_token(json.getString("access_token"));
			token.setExpires_in(json.getInt("expires_in"));
			jedis.set("access_token", json.getString("access_token"));
			jedis.expire("access_token", 60 * 60 * 2);
		}
		RedisUtil.returnResource(jedis);
		return token;
	}

	/**
	 * 获取凭证
	 * 
	 * @return
	 */
	public static String getAccess_Token() {
		System.out.println("从缓存中读取");
		Jedis jedis = RedisUtil.getJedis();
		String access_token = jedis.get("access_token");
		if (access_token == null) {
			AccessToken token = getAccessToken();
			access_token = token.getAccess_token();
		}
		RedisUtil.returnResource(jedis);
		return access_token;
	}

	public static void main(String[] args) {
		System.out.println(getAccess_Token());
	}

}
