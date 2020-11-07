package com.ksh.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;
// http://localhost:8080/login
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService의 loadUserByUsername");
		User userEntity = userRepository.findByUsername(username);
		System.out.println("userEntity :"+userEntity);
		return new PrincipalDetails(userEntity);
	}
}
