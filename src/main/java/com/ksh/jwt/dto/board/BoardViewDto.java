package com.ksh.jwt.dto.board;

import java.sql.Timestamp;
import java.util.List;

import com.ksh.jwt.model.Problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardViewDto {
	private int id;
	private String title;
	private String content;
	private String image;
	private int count;
	private List<Problem> problems;
	private int userId;
	private String username;
	private Timestamp createDate;
}
