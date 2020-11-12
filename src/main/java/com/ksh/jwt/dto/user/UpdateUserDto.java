package com.ksh.jwt.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
	private String username;
	private String password;
	private String nPassword;
	private String nPassword2;
}
