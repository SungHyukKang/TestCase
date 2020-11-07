package com.ksh.jwt.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.config.jwt.JwtProperties;
import com.ksh.jwt.dto.ResponseDto;
import com.ksh.jwt.dto.ResponseEntityDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.UserRepository;
import com.ksh.jwt.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
	
	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
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
