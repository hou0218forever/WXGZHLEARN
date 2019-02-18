package com.hzb.controller;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hzb.util.TextMessageUtil;
import com.hzb.util.WXUtil;

@RestController
public class WXController {
	private static final Logger log = LoggerFactory.getLogger(WXController.class);
	private final String TOKEN = "houzhibo";

	/**
	 * 接收wx传递的数据或校验接口信息配置
	 */
	@GetMapping(value = { "/validateWx" })
	public String doget(HttpServletRequest request) throws IOException {
		log.info("-----开始校验签名-----");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce"); // 随机数
		String echostr = request.getParameter("echostr");// 随机字符串
		String sortStr = WXUtil.sort(TOKEN, timestamp, nonce);
		String mySignature = WXUtil.shal(sortStr);
		if (!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)) {
			log.info("-----签名校验通过-----");
			return echostr;
		} else {
			log.info("-----校验签名失败-----");
			return null;
		}
	}

	@PostMapping(value = { "/validateWx" })
	public String dopost(HttpServletRequest request) {
		// 将微信请求xml转为map格式，获取所需的参数
		Map<String, String> map = WXUtil.xmlToMap(request);
		String ToUserName = map.get("ToUserName");
		String FromUserName = map.get("FromUserName");
		String MsgType = map.get("MsgType");
		String Content = map.get("Content");

		String message = null;
		if ("text".equals(MsgType)) {
			TextMessageUtil textMessage = new TextMessageUtil();
			message = textMessage.initMessage(FromUserName, ToUserName, Content);
		}
		return message;
	}

	
}
