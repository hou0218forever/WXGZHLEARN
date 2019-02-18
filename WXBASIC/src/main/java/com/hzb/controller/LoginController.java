package com.hzb.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hzb.translate.TransApi;
import com.hzb.util.CheckUtil;
import com.hzb.util.ImageMessageUtil;
import com.hzb.util.MessageUtil;
import com.hzb.util.TextMessageUtil;

/**
 * 
 * 类名称: LoginController 类描述: 与微信对接登陆验证
 * 
 * @author hzb 创建时间:2017年12月5日上午10:52:13
 */
@RequestMapping("/wx")
@RestController
public class LoginController {
	@RequestMapping(value = "validateWx", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		System.out.println("success");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			return echostr;
		}
		return null;
	}

	@RequestMapping(value = "validateWx", method = RequestMethod.POST)
	public String dopost(HttpServletRequest request) {
		// 将微信请求xml转为map格式，获取所需的参数
		Map<String, String> map = MessageUtil.xmlToMap(request);
		String ToUserName = map.get("ToUserName");
		String FromUserName = map.get("FromUserName");
		String MsgType = map.get("MsgType");
		String Content = map.get("Content");
		

		String message = null;
		// 处理文本类型，实现输入1，回复相应的封装的内容
		if ("text".equals(MsgType)) {
			if ("1".equals(Content)) {
				TextMessageUtil textMessage = new TextMessageUtil();
				message = textMessage.initMessage(FromUserName, ToUserName);
			} else if (Content.startsWith("翻译")) {
				// 设置翻译的规则，以翻译开头
				TextMessageUtil textMessage = new TextMessageUtil();
				// 将翻译开头置为""
				String query = Content.replaceAll("^翻译", "");
				if (query == "") {
					// 如果查询字段为空，调出使用指南
					message = textMessage.initMessage(FromUserName, ToUserName);
				} else {
					message = textMessage.initMessage(FromUserName, ToUserName, TransApi.getTransResult(query));
				}
			} else {
				TextMessageUtil textMessage = new TextMessageUtil();
				message = textMessage.initMessage(FromUserName, ToUserName, Content);
			}
		}
		//处理图片
		else if ("image".equals(MsgType)) {
			String mediaId = map.get("MediaId");
			ImageMessageUtil util = new ImageMessageUtil();
			message = util.initMessage(FromUserName, ToUserName, mediaId);
		}
		return message;
	}
}
