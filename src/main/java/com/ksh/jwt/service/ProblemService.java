package com.ksh.jwt.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksh.jwt.dto.problem.MySolvedDto;
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
	
	@Transactional(readOnly=true)
	public Problem view(int problemId) {
		Problem problem = problemRepository.findById(problemId).orElseThrow(()->{
			return new IllegalArgumentException("문제가 존재하지 않습니다.");
		});
		return problem;
	}
	
	
	
	@Transactional(readOnly = true)
	public HashMap<Integer,List<MySolvedDto>> mySolved(List<String> solvedList) {
		Collections.sort(solvedList);
		HashMap<Integer,List<MySolvedDto>> hsmap =new HashMap<>();
		for(String X : solvedList) {
			int z =Integer.parseInt(X);
			Problem pro = problemRepository.findById(z).orElseThrow(()->{
				return new IllegalArgumentException("말이 안되는 오류");
			});
			int boardId=pro.getBoard().getId();
			if(hsmap.get(boardId)==null){
				hsmap.put(boardId,new ArrayList<>());
			}
			hsmap.get(boardId).add(new MySolvedDto(z,pro.getTitle()));
		}
		return hsmap;
	}
}
