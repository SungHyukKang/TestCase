package com.ksh.jwt.dto.problem;

import java.util.List;

import com.ksh.jwt.dto.user.PairDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VsDto {
	private List<PairDto> AllList;
	private List<PairDto> mySolvedList;
	private List<PairDto> vsSolvedList;
}
