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
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.repository.ProblemRepository;
import com.ksh.jwt.repository.UserRepository;

@Service
//@RequiredArgsConstructor -> @Authwired 생략 가능 final로 선언
public class ProblemService {
	@Autowired
	private ProblemRepository problemRepository;
	@Autowired 
	private BoardRepository boardRepository;
	
	@Autowired 
	private UserRepository userRepository;
	@Transactional
	public void write(int boardId,List<Problem> problems,String username) {
		Board board =boardRepository.findById(boardId).get();
		if(username.equals(board.getUser().getUsername())){
			for(Problem p : problems) {
			if(p.getTitle()==null||p.getTitle().equals(""))
				continue;
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
	
	@Transactional
	public void delete(String username, int problemId) {
		int boardId = problemRepository.findById(problemId).get().getBoard().getId();
		System.out.println(boardRepository.findById(boardId).get().getUser().getUsername());
		if(!boardRepository.findById(boardId).get().getUser().getUsername().equals(username)) {
			throw new IllegalArgumentException("문제 작성자가 아닙니다.");
		}
		List<User> users = userRepository.findAll();
		HashMap<Integer,Boolean> hsmap =new HashMap<>();
		hsmap.put(problemId, true);
		for(User u : users) {
			if(u==null)
				continue;
			String sol = u.getSolved();
			String wro = u.getWrong();
			for(String p : u.getSolvedList()) {
				if(hsmap.get(Integer.parseInt(p))!=null&&hsmap.get(Integer.parseInt(p)))
					sol =replacePerfect(sol,p);
			}
			for(String w : u.getWrongList()) {
				if(hsmap.get(Integer.parseInt(w))!=null&&hsmap.get(Integer.parseInt(w)))
					wro =replacePerfect(wro,w);
			}
			u.setSolved(sol);
			u.setWrong(wro);
		}
		problemRepository.deleteById(problemId);
	}
	public  String replacePerfect(String str,String change) {
		StringBuilder sb = new StringBuilder();
		for(String X : str.split(" ")) {
			if(X.equals(change)) {
				continue;
			}
			sb.append(X+" ");
		}
		return sb.toString().trim();
	}
}