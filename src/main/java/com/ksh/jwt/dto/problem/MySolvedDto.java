package com.ksh.jwt.dto.problem;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySolvedDto {
	private Integer problemId;
	private String title;
}

