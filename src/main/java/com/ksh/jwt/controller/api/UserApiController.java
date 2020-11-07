package com.ksh.jwt.controller.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptpasswordEncoder;
	
	
	//회원가입.
	@PostMapping("join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptpasswordEncoder.encode(user.getPassword()));//비밀번호 암호화 .
		user.setRoles("ROLE_USER");//해당 유저의 권한 
		userRepository.save(user);
		return "회원가입완료";
	}
}
