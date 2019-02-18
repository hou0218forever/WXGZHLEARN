package com.hzb.util;

import java.util.Date;

import com.hzb.entity.MessageText;
import com.thoughtworks.xstream.XStream;

public class TextMessageUtil {
	/**
	 * 将发送消息封装成对应的xml格式
	 */
	public String messageToxml(MessageText message) {
		XStream xstream = new XStream();
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}

	/**
	 * 封装发送消息对象,封装时，需要将调换发送者和接收者的关系
	 * 
	 * @param FromUserName
	 * @param ToUserName
	 * @param content
	 */
	public String initMessage(String FromUserName, String ToUserName, String content) {
		MessageText text = new MessageText();
		text.setToUserName(FromUserName);
		text.setFromUserName(ToUserName);
		text.setContent(content);
		text.setCreateTime(new Date().getTime());
		text.setMsgType("text");
		return messageToxml(text);
	}
}
