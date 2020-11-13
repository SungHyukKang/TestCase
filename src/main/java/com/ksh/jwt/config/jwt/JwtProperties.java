package com.ksh.jwt.config.jwt;

public interface JwtProperties {
	String SECRET ="kshlmckjyyhj";
	int EXPIRATION_TIME =60000000*10;
	String TOKEN_PREFIX ="Bearer ";
	String HEADER_STRING ="Authorization";
	String REFRESH_STRING ="RefreshToken";
}