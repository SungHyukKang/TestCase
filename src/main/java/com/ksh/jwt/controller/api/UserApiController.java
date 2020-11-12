package com.ksh.jwt.controller.api;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.common.ResponseDto;
import com.ksh.jwt.dto.problem.SolvedDto;
import com.ksh.jwt.dto.user.UpdateUserDto;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.ProblemRepository;
import com.ksh.jwt.repository.UserRepository;
import com.ksh.jwt.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserRepository userRepository;
	private final ProblemRepository problemRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserService userService;
	
	@PostMapping("/solvedCheck")
	public ResponseDto<String> solvedCheck(@RequestBody SolvedDto solved,@AuthenticationPrincipal PrincipalDetails principal){
		problemRepository.findById(solved.getProblemId()).orElseThrow(()->{
			return new IllegalArgumentException("문제가 존재하지 않습니다.");
		});
		userService.solvedCheck(solved.getProblemId(),solved.getSolvedStatus(),principal.getUser().getId());
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
	
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
		user.setSolved("");
		user.setWrong("");
		userRepository.save(user);
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
}