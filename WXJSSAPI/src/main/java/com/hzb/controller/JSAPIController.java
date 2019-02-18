package com.hzb.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hzb.bean.Jsapi;
import com.hzb.util.JsapiTicketUtil;
import com.hzb.util.Sign;

@Controller // 注意这里必须为Controller
public class JSAPIController {

	@GetMapping("/")
	public String helloHtml(Model model) {
		return "demo";
	}
	
	/**
	 * @Description 调用微信jssdk（成功之后要打开的界面！）
	 * @Creat date 2018-12-1
	 * @author hzb
	 * @return demo.html
	 */
	@GetMapping("getJsAPI")
	public String getJsAPI(Model model) {
		
		String jsapi_ticket =JsapiTicketUtil.getJSApiTicket();
		String url="http://hzb.nat300.top/getJsAPI";   //url是你当前访问的界面的url
        Map<String,String> map = Sign.sign(jsapi_ticket, url);
        System.out.println(map);
        Jsapi jsapi=new Jsapi();
        jsapi.setTimestamp(map.get("timestamp"));
        jsapi.setNonceStr(map.get("nonceStr"));
        jsapi.setSignature(map.get("signature"));
        jsapi.setAppId("wxe7c0538d78f13ed9");
		System.out.println(jsapi);
		model.addAttribute("jsapi",jsapi);
		return "demo";
	}

	@PostMapping("getJsAPI")
	@ResponseBody
	public Map<String, String> getJsAPIClick(Model model) {
		
		String jsapi_ticket =JsapiTicketUtil.getJSApiTicket();
		String url="http://hzb.nat300.top/getJsAPI";   //url是你当前访问的界面的url
        Map<String,String> map = Sign.sign(jsapi_ticket, url);
        
		return map;
	}

	public static void main(String[] args) {
		String jsapi_ticket =JsapiTicketUtil.getJSApiTicket();
		try {
			String[] sign = Sign.getSign(jsapi_ticket);
			System.out.println(sign);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
