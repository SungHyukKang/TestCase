package com.ksh.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.UserRepository;

// 시큐리티가 filter를 가지고 있는데 , 그 필터중에 BasicAuthenticatinoFilter 라는 것이 있음.
//권한이나 인증이 필요한 특정 주소를 요청했을 때 ,위 필터를 무조건 타게 되어있음.
//만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository=userRepository;
		
	}
	//인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("인증이나 권한이 필요한 주소 요청됨");
		String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("jwtHeader : "+jwtHeader);
		//header가 있는지 확인
		if(jwtHeader==null || !jwtHeader.startsWith("Bearer")) {
			System.out.println("!!권한 없음 (토큰 X)!!");
			chain.doFilter(request, response);
			return;
		}
		//JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace("Bearer ", "");
		
		String username = 
				JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();
		//서명이 정상적으로 되었다면
		if(username!=null) {
			User userEntity = userRepository.findByUsername(username);
			HttpSession session = request.getSession();
			
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			//Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
			session.setAttribute("principal", principalDetails);
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities());
			
			//강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장한다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
			chain.doFilter(request, response);
		}
	}
}