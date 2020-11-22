package com.ksh.jwt.dto.problem;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VsDto {
	private String[] AllList;
	private String[] mySolvedList;
	private String[] vsSolvedList;
}
