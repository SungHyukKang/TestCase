package com.ksh.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.ksh.jwt.config.auth.PrincipalDetailService;
import com.ksh.jwt.config.jwt.JwtAuthenticationFilter;
import com.ksh.jwt.config.jwt.JwtAuthorizationFilter;
import com.ksh.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserRepository userRepository;
	private final CorsFilter corsFilter;
	private final PrincipalDetailService principalDetailService;
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//STATELESS -> 세션을 사용하지 않겠다 .
		.and()
		.addFilter(corsFilter) //@CrossOrigin(인증 X) , 시큐리티 필터에 등록 안됨(인증 O)
		.formLogin()
		.defaultSuccessUrl("/admin")
		.and()
		.httpBasic().disable()
		.addFilter(new JwtAuthenticationFilter(authenticationManager())) //AuthenticationManager
		.addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository)) //AuthenticationManager
		.authorizeRequests()
//		.antMatchers("/join/**","/index/**","/findPw","/findUsername").permitAll()
		.antMatchers("/api/v1/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/manager/**")
			.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//		.antMatchers("/**")
//			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/board/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			
			.anyRequest().permitAll();
		
			;
		
	}
}
