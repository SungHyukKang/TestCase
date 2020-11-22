package com.ksh.jwt.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//모든 API컨트롤러는 ResponseDto로 리턴 .
public class ResponseDto<T> {
	int status;
	T data;
}