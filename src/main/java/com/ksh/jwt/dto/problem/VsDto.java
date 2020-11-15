package com.ksh.jwt.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VsDto {
	private String All;
	private String mySolved;
	private String vsSolved;
}
