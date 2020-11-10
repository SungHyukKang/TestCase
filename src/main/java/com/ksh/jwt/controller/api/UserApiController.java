package com.ksh.jwt.controller.api;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.ResponseDto;
import com.ksh.jwt.dto.UpdateUserDto;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.UserRepository;
import com.ksh.jwt.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserService userService;
	
	//회원가입.
	@PutMapping("update")
	public ResponseDto<String> update(@RequestBody UpdateUserDto updateUser,@AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println(updateUser.getNPassword());
		System.out.println(updateUser);
		userService.update(updateUser,principal.getUser());
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
	
	@PostMapping("join")
	public ResponseDto<String> join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//비밀번호 암호화 .
		user.setRoles("ROLE_USER");//해당 유저의 권한 
		userRepository.save(user);
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
}