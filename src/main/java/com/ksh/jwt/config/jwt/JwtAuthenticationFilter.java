package com.ksh.jwt.config.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.model.User;

import lombok.RequiredArgsConstructor;
//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
//login 요청해서 username , password 전송하면(post)
// UsernamePasswordAuthenticationFilter 동작을 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	//login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter: 로그인 시도중");
		// 1. username , password 받아서
		try {
//			BufferedReader br = request.getReader();
//			
//			String input =null;
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(),User.class);
			System.out.println(user);
			UsernamePasswordAuthenticationToken authenticationToken=
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			//PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨
			//DB에 있는 username과 password가 일치한다.
			System.out.println(authenticationToken);
			Authentication authentication=
					authenticationManager.authenticate(authenticationToken);
			//authenticate() 함수가 호출되면 인증 프로바이더가 UserDetailService의 loadUserByUsername(토큰의 첫번째 파라미터)을 호출하고
			//UserDetails를 리턴 받아서 토큰의 두번째 파라미터(credential)과 UserDetails(DB값)의 getPassword()와 비교해서 동일하면
			//Authentication 객체를 만들어서 필터체인으로 리턴해준다.
			//결론 : 인증 프로바이더에게 알려 줄 필요 없음.
			//authentication 객체를 session 영역에 저장 해야하고 그 방법이 return 해주면 됨
			//리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임
			//굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음.근데 단지 권한 처리때문에 session에 넣어준다.
			//
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.println("로그인 완료 :"+principalDetails.getUser().getUsername());
			System.out.println(authentication);
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthenticaton 함수가 실행된다.
	//JWT 토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response해주면 됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfultAuthenticat 실행됨: 인증 완료");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		//RSA방식은 아니고 , Hash암호 방식
		PrintWriter out = response.getWriter();
		String jwtToken = JWT.create()
				.withSubject("토큰")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		String refToken = JWT.create()
				.withSubject("토큰")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME*1000))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);
		response.addHeader(JwtProperties.REFRESH_STRING,JwtProperties.TOKEN_PREFIX+refToken);
		out.print("{\n");
		out.print("\"accessToken\""+":"+" \""+jwtToken+"\"");
		out.print(",\n\"refreshToken\""+":"+" \""+refToken+"\"");
		out.print(",\n\"expireIn\""+":"+" \""+"600000"+"\"");
		out.print("\n}");
		out.flush();
		out.close();
	}
	
}