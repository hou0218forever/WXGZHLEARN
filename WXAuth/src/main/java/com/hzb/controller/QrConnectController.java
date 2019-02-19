package com.hzb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hzb.bean.WeiXinUser;
import com.hzb.constants.ProjectConst;
import com.hzb.service.WeiXinUserInfoService;
import com.hzb.util.AccessTokenUtil;

/**
 * @author hzb
 * @create 2018-01-18 17:47
 * @desc 扫描二维码
 **/
@RestController
public class QrConnectController {

	@Autowired
	private WeiXinUserInfoService userService;

	@RequestMapping("/qrconnect")
	public void login(HttpServletResponse response) {
		// 这里是回调的url
		try {
			String redirect_uri = URLEncoder.encode("http://zhuoxin.nat300.top/callback", "UTF-8");
			String url = ProjectConst.CONNECT_QRCONNECT;

			String replace = url.replace("APPID", ProjectConst.PROJECT_APPID).replace("REDIRECT_URI", redirect_uri);
			response.sendRedirect(replace);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 进行网页授权，便于获取到用户的绑定的内容
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/callback")
	public WeiXinUser callback(HttpServletRequest request, HttpSession session) {
		// 首先判断一下session中，是否有保存着的当前用户的信息，有的话，就不需要进行重复请求信息
		WeiXinUser weiXinUser = null;
		if (session.getAttribute("currentUser") != null) {
			weiXinUser = (WeiXinUser) session.getAttribute("currentUser");
		} else {
			/**
			 * 进行获取openId，必须的一个参数，这个是当进行了授权页面的时候，再重定向了我们自己的一个页面的时候，
			 * 会在request页面中，新增这个字段信息，要结合这个ProjectConst.Get_WEIXINPAGE_Code这个常量思考
			 */
			String code = request.getParameter("code");
			try {
				// 得到当前用户的信息(具体信息就看weixinUser这个javabean)
				weiXinUser = getTheCode(session, code);
				// 将获取到的用户信息，放入到session中
				session.setAttribute("currentUser", weiXinUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return weiXinUser;
	}

	/**
	 * 获取用户的openId
	 * 
	 * @param session
	 * @param code
	 * @return 返回封装的微信用户的对象
	 */
	private WeiXinUser getTheCode(HttpSession session, String code) {
		Map<String, String> authInfo = new HashMap<>();
		String openId = "";
		if (code != null) {
			// 调用根据用户的code得到需要的授权信息
			authInfo = userService.getAuthInfo(code);
			// 获取到openId
			openId = authInfo.get("Openid");
		}
		// 获取基础刷新的接口访问凭证（目前还没明白为什么用authInfo.get("AccessToken");这里面的access_token就不行）
		String accessToken = AccessTokenUtil.getAccess_Token();
		// 获取到微信用户的信息
		WeiXinUser userinfo = userService.getUserInfo(accessToken, openId);

		return userinfo;
	}
}
