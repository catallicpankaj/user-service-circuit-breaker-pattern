package com.sample.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String swagger() {
		//return "redirect:swagger-ui.html"; With non-webflux
		return "redirect:/swagger-ui/index.html";
	}
}
