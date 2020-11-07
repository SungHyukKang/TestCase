package com.ksh.jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestEntityDto {
	String username;
	String password;
}
