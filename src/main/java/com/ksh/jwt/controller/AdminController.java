package com.ksh.jwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	
	
	@GetMapping("/NewFile")
	public String testest() {
		return "NewFile";
	}
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
}