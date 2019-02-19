package com.hzb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hzb.util.HttpUtil;
import net.sf.json.JSONObject;

/**
 * @author hzb
 * @create 2018-01-18 17:47
 * @desc 扫描二维码
 **/
@Controller
public class QrConnectController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	CharSequence appid = "wx90dcf00c1aae2b78";
	CharSequence appsecret = "bfb2b96475ef0e96e7438aa1500f24b2";
	CharSequence scope = "snsapi_login";
	String callBack = "http://zhuoxin.nat300.top/callBack";

	@RequestMapping("/qrconnect")
	public void login(HttpServletResponse response) {
		try {
			String oauthUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
			String redirect_uri = URLEncoder.encode(callBack, "utf-8");
			oauthUrl = oauthUrl.replace("APPID", appid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope);
			System.out.println(oauthUrl);
			response.sendRedirect(oauthUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/")
	public String index(Model model) throws UnsupportedEncodingException {
		String oauthUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		String redirect_uri = URLEncoder.encode(callBack, "utf-8");
		oauthUrl = oauthUrl.replace("APPID", appid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope);
		model.addAttribute("name", "hzb");
		model.addAttribute("oauthUrl", oauthUrl);
		return "index";
	}

	/**
	 * 自定义
	 */
	@RequestMapping("/1")
	public String index1(Model model) throws UnsupportedEncodingException {
		String redirect_uri = URLEncoder.encode(callBack, "utf-8");
		model.addAttribute("name", "hzb");
		model.addAttribute("appid", appid);
		model.addAttribute("scope", scope);
		model.addAttribute("redirect_uri", redirect_uri);
		return "index1";
	}

	/**
	 * 进行网页授权，便于获取到用户的绑定的内容
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/callBack")
	public String callBack(String code, String state, Model model) throws Exception {
		logger.info("进入授权回调,code:{},state:{}", code, state);

		// 1.通过code获取access_token
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

		url = url.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
		JSONObject tokenInfoStr = HttpUtil.doGetstr(url);

		JSONObject tokenInfoObject = JSONObject.fromObject(tokenInfoStr);
		logger.info("tokenInfoObject:{}", tokenInfoObject);

		// 2.通过access_token和openid获取用户信息
		String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
		userInfoUrl = userInfoUrl.replace("ACCESS_TOKEN", tokenInfoObject.getString("access_token")).replace("OPENID",
				tokenInfoObject.getString("openid"));
		JSONObject userInfoStr = HttpUtil.doGetstr(userInfoUrl);
		logger.info("userInfoObject:{}", userInfoStr);

		model.addAttribute("tokenInfoObject", tokenInfoObject);
		model.addAttribute("userInfoObject", userInfoStr);

		return "result";
	}

}
