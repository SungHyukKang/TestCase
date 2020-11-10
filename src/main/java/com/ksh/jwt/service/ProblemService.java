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
	public void write(int boardId,Problem[] problems,String username) {
		System.out.println("!!");
		Board board =boardRepository.findById(boardId).get();
		if(username.equals(board.getUser().getUsername())){
			for(Problem p : problems) {
			System.out.println(p.toString());
			String title =p.getTitle();
			String num1 =p.getNum1();
			String num2 =p.getNum2();
			String num3 =p.getNum3();
			String num4 =p.getNum4();
//			String num5 =problem.getNum5();
			String answer =p.getAnswer();
			problemRepository.write(title, num1, num2, num3, num4,  boardId, answer);
			}
		}else {
			throw new IllegalArgumentException("글 작성자가 아닙니다.");
		}
	}
}
