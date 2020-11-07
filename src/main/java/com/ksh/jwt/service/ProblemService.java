package com.ksh.jwt.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksh.jwt.model.Problem;
import com.ksh.jwt.repository.ProblemRepository;

@Service
public class ProblemService {
	@Autowired
	private ProblemRepository problemRepository;
	
	@Transactional
	public void write(int boardId,Problem problem) {
		String title =problem.getTitle();
		String num1 =problem.getNum1();
		String num2 =problem.getNum2();
		String num3 =problem.getNum3();
		String num4 =problem.getNum4();
		String num5 =problem.getNum5();
		String answer =problem.getAnswer();
		problemRepository.write(title, num1, num2, num3, num4, num5, boardId, answer);
	}
	
}
