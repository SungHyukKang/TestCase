package com.ksh.jwt.dto.problem;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolvedResponseDto {
	private int boardId;
	private List<MySolvedDto> problems;
}
