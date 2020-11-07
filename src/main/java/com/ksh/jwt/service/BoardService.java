package com.ksh.jwt.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksh.jwt.model.Board;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.BoardRepository;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	
	@Transactional
	public void save(Board board , User user) {
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly =true)
	public Page<Board> list(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	public Board view(int id) {
		Board board = boardRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("글을 읽어올 수 없습니다.");
		});
		return board;
	}

	public List<Board> search(Pageable pageable,String keyword) {
		List<Board> list = boardRepository.findByTitleContaining(keyword);
		return list;
	}
}
