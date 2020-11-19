package com.ksh.jwt.dto.board;

import java.util.List;

import org.springframework.data.domain.Pageable;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardPagingViewDto {
	private List<BoardViewDto> contents;
	private Pageable paging;
	private int totalPages;
	private long elementSizes;
	private long totalElements;
}
