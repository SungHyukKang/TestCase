package com.ksh.jwt.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
	
	
	@GetMapping("home")
	public String home() {
		return "admin";
	}
	
	@PostMapping("token")
	public String token() {
		return "<h1>token</h1>";
	}
	
	@GetMapping("/api/v1/user")
	public String user(@RequestBody User user) {
		System.out.println(user.getUsername()+" " +user.getPassword());
		return "user";
	}
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}
	
}
