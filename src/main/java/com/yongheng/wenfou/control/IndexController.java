package com.yongheng.wenfou.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(value = { "/", "/index" })
	public String index() {
		return "index";
	}


}
