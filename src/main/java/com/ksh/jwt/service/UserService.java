package com.ksh.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void update(UpdateUserDto updateUser,User user) {
		String np=bCryptPasswordEncoder.encode(updateUser.getPassword());
		User u = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("아이디를 찾을 수 없습니다.");
		});
		if(updateUser.getPassword()!=null&&bCryptPasswordEncoder.matches(updateUser.getPassword(),u.getPassword())) {
			System.out.println("1111111111111111111111111111111111");
			if(updateUser.getNPassword().equals(updateUser.getNPassword2())) {
				String newPw = bCryptPasswordEncoder.encode(updateUser.getNPassword());
				u.setPassword(newPw);
				System.out.println("222222222222222222222222222222");
				userRepository.save(u);
			}else {
				throw new IllegalArgumentException("변경할 비밀번호가 서로 다릅니다");
			}
		}else {
			throw new IllegalArgumentException("현재 패스워드가 다릅니다");
		}
	}
	
	
}
