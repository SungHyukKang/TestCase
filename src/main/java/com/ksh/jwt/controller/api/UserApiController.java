package com.ksh.jwt.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.board.BoardViewDto;
import com.ksh.jwt.dto.common.ResponseDto;
import com.ksh.jwt.dto.email.EmailCheckDto;
import com.ksh.jwt.dto.email.MailDto;
import com.ksh.jwt.dto.problem.SolvedDto;
import com.ksh.jwt.dto.problem.VsDto;
import com.ksh.jwt.dto.user.EnteredPwDto;
import com.ksh.jwt.dto.user.FindPwDto;
import com.ksh.jwt.dto.user.PairDto;
import com.ksh.jwt.dto.user.UpdateUserDto;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.ProblemRepository;
import com.ksh.jwt.repository.UserRepository;
import com.ksh.jwt.service.EmailService;
import com.ksh.jwt.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {

	private final UserRepository userRepository;
	private final ProblemRepository problemRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserService userService;
	private final EmailService emailService;

	@PostMapping("/enteredPw")
	public ResponseDto<Boolean> enteredPw(@RequestBody EnteredPwDto user,
			@AuthenticationPrincipal PrincipalDetails principal) {

		if (bCryptPasswordEncoder.matches(user.getPassword(), principal.getPassword())) {
			return new ResponseDto<Boolean>(HttpStatus.OK.value(), true);
		} else {
			return new ResponseDto<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR.value(), false);
		}
	}

	@PostMapping("solvedCheck")
	public ResponseDto<String> solvedCheck(@RequestBody SolvedDto solved,
			@AuthenticationPrincipal PrincipalDetails principal) {
		problemRepository.findById(solved.getProblemId()).orElseThrow(() -> {
			return new IllegalArgumentException("문제가 존재하지 않습니다.");
		});
		userService.solvedCheck(solved.getProblemId(), solved.getSolvedStatus(), principal.getUser().getId());
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	@DeleteMapping("/userDelete")
	public ResponseDto<String> userDelete(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody User user) {
		if (bCryptPasswordEncoder.matches(user.getPassword(), principal.getPassword())) {
			userService.deleteInfo(principal.getUser().getId());
			userRepository.delete(principal.getUser());
		}
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	@GetMapping("/favoriteList")
	public List<BoardViewDto> favoriteList(@AuthenticationPrincipal PrincipalDetails principal) {
		List<BoardViewDto> list = userService.favoriteList(principal.getUser().getFavoriteList());
		return list;
	}

	@GetMapping("/favorite/{boardId}")
	public ResponseDto<String> addFavorite(@AuthenticationPrincipal PrincipalDetails principal,
			@PathVariable int boardId) {
		int userId = principal.getUser().getId();
		userService.addFavorite(userId, boardId);

		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	// 회원가입.
	@PutMapping("update")
	public ResponseDto<String> update(@RequestBody UpdateUserDto user,
			@AuthenticationPrincipal PrincipalDetails principal) {
		userService.update(user.getNPassword(), principal.getUser());
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	@GetMapping("vs/{vsUsername}")
	public VsDto vsView(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable String vsUsername) {
		VsDto vs = new VsDto();
		String mySolved = principal.getUser().getSolved();
		String vsSolved = userService.vsView(vsUsername);

		Map<String, Integer> hsmap = new LinkedHashMap<>();
		for (String X : mySolved.split(" ")) {
			hsmap.put(X, 1);
		}
		for (String X : vsSolved.split(" ")) {
			hsmap.put(X, hsmap.getOrDefault(X, 3) + 1);
		}
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		for (String key : hsmap.keySet()) {
			if (hsmap.get(key) == 1) {
				sb1.append(key + " ");
			} else if (hsmap.get(key) == 4) {
				sb2.append(key + " ");
			} else {
				sb3.append(key + " ");
			}
		}
		List<PairDto> sb3list = new ArrayList<>();
		List<PairDto> sb1list = new ArrayList<>();
		List<PairDto> sb2list = new ArrayList<>();
		if (!sb3.toString().trim().equals(""))
		for (String X : sb3.toString().trim().split(" ")) {
			int boardId=problemRepository.findById(Integer.parseInt(X)).get().getBoard().getId();
			sb3list.add(new PairDto(boardId,Integer.parseInt(X)));
		}	
		if (!sb1.toString().trim().equals(""))
		for (String X : sb1.toString().trim().split(" ")) {
			int boardId=problemRepository.findById(Integer.parseInt(X)).get().getBoard().getId();
			sb1list.add(new PairDto(boardId,Integer.parseInt(X)));
		}
		if (!sb2.toString().trim().equals(""))
		for (String X : sb2.toString().trim().split(" ")) {
			int boardId=problemRepository.findById(Integer.parseInt(X)).get().getBoard().getId();
			sb2list.add(new PairDto(boardId,Integer.parseInt(X)));
		}
		if (!sb3.toString().trim().equals(""))
			vs.setAllList(sb3list);
		if (!sb1.toString().trim().equals(""))
			vs.setMySolvedList(sb1list);
		if (!sb2.toString().trim().equals(""))
			vs.setVsSolvedList(sb2list);
		return vs;
	}

	@PostMapping("join")
	public ResponseDto<String> join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));// 비밀번호 암호화 .
		user.setRoles("ROLE_USER");// 해당 유저의 권한
		user.setSolved("");
		user.setWrong("");
		user.setFavorite("");
		userRepository.save(user);
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	@GetMapping("/join/idCheck")
	public boolean idCheck(@RequestParam("username") String username) {
		boolean tf = userService.idCheck(username);
		return tf;
	}

	@PostMapping("/findUsername")
	public ResponseDto<String> findUsername(@RequestBody EmailCheckDto emailCheckDto) {
		String username = userService.userEmailCheck(emailCheckDto.getEmail());
		if (username != null) {
			emailService.sendEmailId(username, emailCheckDto.getEmail());
			return new ResponseDto<String>(HttpStatus.OK.value(), "1");
		} else {
			throw new IllegalArgumentException("이메일로 가입된 계정이 존재하지 않습니다.");
		}
	}

	@PostMapping("/findPw")
	public Map<String, Boolean> findPw(@RequestBody FindPwDto pwDto) {
		Map<String, Boolean> map = new HashMap<>();
		boolean pwFindCheck = userService.userEmailCheck(pwDto.getUsername(), pwDto.getEmail());
		map.put("check", pwFindCheck);
		System.out.println("check완료");
		return map;
	}

	@PostMapping("/join/emailCheck")
	public Map<String, String> emailCheck(@RequestBody EmailCheckDto email) {
		System.out.println(email.getEmail());
		MailDto dto = emailService.checkEmail(email.getEmail());
		emailService.mailSend(dto);
		Map<String, String> hsmap = new HashMap<>();
		hsmap.put("authKey", dto.getAuthKey());
		return hsmap;
	}

	@PostMapping("findPw/sendEmail")
	public ResponseDto<String> sendEmail(@RequestBody FindPwDto pwDto) {
		MailDto dto = emailService.createMailAndChangePassword(pwDto.getEmail(), pwDto.getUsername());
		emailService.mailSend(dto);
		System.out.println("메일 보내기 완료");
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

}