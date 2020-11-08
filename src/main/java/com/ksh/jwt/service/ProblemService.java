package com.ksh.jwt.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksh.jwt.model.Board;
import com.ksh.jwt.model.Problem;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.repository.ProblemRepository;

@Service
public class ProblemService {
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired 
	private BoardRepository boardRepository;
	
	@Transactional
	public void write(int boardId,Problem problem,String username) {
		Board board =boardRepository.findById(boardId).get();
		if(username.equals(board.getUser().getUsername())){
			String title =problem.getTitle();
			String num1 =problem.getNum1();
			String num2 =problem.getNum2();
			String num3 =problem.getNum3();
			String num4 =problem.getNum4();
			String num5 =problem.getNum5();
			String answer =problem.getAnswer();
			problemRepository.write(title, num1, num2, num3, num4, num5, boardId, answer);
		}else {
			throw new IllegalArgumentException("글 작성자가 아닙니다.");
		}
	}
}
