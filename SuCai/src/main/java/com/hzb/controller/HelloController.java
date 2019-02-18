package com.hzb.controller;

import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 注意这里必须为Controller
public class HelloController {

	@RequestMapping("/test1")
	public String helloHtml(HashMap<String, Object> map) {
		map.put("hello", "欢迎进入HTML页面");
		return "/demo1";
	}

	@RequestMapping("/test2")
	public String helloHtml2(HashMap<String, Object> map) {
		map.put("hello", "欢迎进入HTML页面");
		return "/demo2";
	}

	@RequestMapping("/test3")
	public String helloHtml3(HashMap<String, Object> map) {
		map.put("hello", "欢迎进入HTML页面");
		return "/demo3";
	}

	@RequestMapping("/test4")
	public String helloHtml4(HashMap<String, Object> map) {
		map.put("hello", "欢迎进入HTML页面");
		return "/demo4";
	}
}
