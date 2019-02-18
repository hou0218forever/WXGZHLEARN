package com.hzb.util;

import java.util.Date;

import com.hzb.bean.Image;
import com.hzb.bean.ImageMessage;
import com.hzb.bean.MessageText;
import com.thoughtworks.xstream.XStream;

/**
* @author: houzhibo
* @date: 2019年2月16日 下午7:18:20
* @describe:
*/
public class ImageMessageUtil {
	/**
	 * 将发送消息封装成对应的xml格式
	 */
	public String messageToxml(ImageMessage message) {
		XStream xstream = new XStream();
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}
	
	public String initMessage(String fromUserName, String toUserName,String mediaId) {
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setToUserName(fromUserName);
		imageMessage.setFromUserName(toUserName);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setMsgType("image");
		Image Image = new Image();
		Image.setMediaId(mediaId);
		imageMessage.setImage(Image);
		return messageToxml(imageMessage);
	}

}
