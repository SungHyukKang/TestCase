package com.ksh.jwt.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.ResponseDto;
import com.ksh.jwt.dto.SolvedDto;
import com.ksh.jwt.dto.UpdateUserDto;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public void update(UpdateUserDto updateUser, User user) {
		User u = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("아이디를 찾을 수 없습니다.");
		});
		if (updateUser.getPassword() != null
				&& bCryptPasswordEncoder.matches(updateUser.getPassword(), u.getPassword())) {
			if (updateUser.getNPassword().equals(updateUser.getNPassword2())) {
				String newPw = bCryptPasswordEncoder.encode(updateUser.getNPassword());
				u.setPassword(newPw);
				userRepository.save(u);
			} else {
				throw new IllegalArgumentException("변경할 비밀번호가 서로 다릅니다");
			}
		} else {
			throw new IllegalArgumentException("현재 패스워드가 다릅니다");
		}
	}

	@Transactional
	public void solvedCheck(int problemId, int solvedStatus, int userId) {
		User u = userRepository.findById(userId).orElseThrow(() -> {
			return new IllegalArgumentException("아이디가 존재하지 않는다.");
		});
		if (solvedStatus == 1) {
			if (!u.getSolved().equals(""))
				for (String X : u.getSolvedList()) {
					if (Integer.parseInt(X) == problemId) {
						return;
					}
				}
			if (!u.getWrong().equals(""))
				for (String X : u.getWrongList()) {
					if (Integer.parseInt(X) == problemId) {
						String x = u.getWrong().replace(X, "");
						x = x.replace("  ", " ");
						x = x.trim();
						u.setWrong(x);
						break;
					}
				}
			if (u.getSolved().equals(""))
				u.setSolved(String.valueOf(problemId));
			else {
				String s = u.getSolved() + " " + problemId;
				s = s.trim();
				u.setSolved(s);
			}
		} else {
			if (!u.getSolved().equals(""))
				for (String X : u.getSolvedList()) {
					if (Integer.parseInt(X) == problemId) {
						return;
					}
				}
			if (!u.getWrong().equals(""))
				for (String X : u.getWrongList()) {
					if (Integer.parseInt(X) == problemId) {
						return;
					}
				}
			if (u.getWrong().equals(""))
				u.setWrong(String.valueOf(problemId));
			else {
				String s = u.getWrong() + " " + problemId;
				s = s.trim();
				u.setWrong(s);
			}
		}
		userRepository.save(u);
	}

}
