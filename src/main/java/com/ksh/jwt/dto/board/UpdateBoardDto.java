package com.ksh.jwt.dto.board;

import java.util.List;

import com.ksh.jwt.model.Problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBoardDto {
	private String title;
	private String content;
	private String image;
	private List<Problem> problems;
	private int userId;
	private String username;
}
