package com.ksh.jwt.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksh.jwt.dto.board.BoardViewDto;
import com.ksh.jwt.dto.user.UpdateUserDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.repository.UserRepository;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public void update(UpdateUserDto updateUser, User user) {
		User u = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("아이디를 찾을 수 없습니다.");
		});
		if (updateUser.getPassword() != null
				&& bCryptPasswordEncoder.matches(updateUser.getPassword(), u.getPassword())) {
			if (updateUser.getNPassword().equals(updateUser.getNPassword2())) {
				String newPw = bCryptPasswordEncoder.encode(updateUser.getNPassword());
				u.setPassword(newPw);
				userRepository.save(u);
			} else {
				throw new IllegalArgumentException("변경할 비밀번호가 서로 다릅니다");
			}
		} else {
			throw new IllegalArgumentException("현재 패스워드가 다릅니다");
		}
	}

	@Transactional
	public void solvedCheck(int problemId, int solvedStatus, int userId) {
		User u = userRepository.findById(userId).orElseThrow(() -> {
			return new IllegalArgumentException("아이디가 존재하지 않는다.");
		});
		if (solvedStatus == 1) {
			if (!u.getSolved().equals(""))
				for (String X : u.getSolvedList()) {
					if (Integer.parseInt(X) == problemId) {
						return;
					}
				}
			if (!u.getWrong().equals(""))
				for (String X : u.getWrongList()) {
					if (Integer.parseInt(X) == problemId) {
						String x = replacePerfect(u.getWrong(),X);
						u.setWrong(x);
						break;
					}
				}
			if (u.getSolved().equals(""))
				u.setSolved(String.valueOf(problemId));
			else {
				String s = u.getSolved() + " " + problemId;
				s = s.trim();
				u.setSolved(s);
			}
		} else {
			if (!u.getSolved().equals(""))
				for (String X : u.getSolvedList()) {
					if (Integer.parseInt(X) == problemId) {
						return;
					}
				}
			if (!u.getWrong().equals(""))
				for (String X : u.getWrongList()) {
					if (Integer.parseInt(X) == problemId) {
						return;
					}
				}
			if (u.getWrong().equals(""))
				u.setWrong(String.valueOf(problemId));
			else {
				String s = u.getWrong() + " " + problemId;
				s = s.trim();
				u.setWrong(s);
			}
		}
		userRepository.save(u);
	}

	public String userEmailCheck(String email) {
		User user = userRepository.findByEmail(email);
		if(user.getUsername()!=null) {
			return user.getUsername();
		}
		return null;
	}
	
	public boolean userEmailCheck(String username, String email) {
		User user = userRepository.findByEmail(email);
		System.out.println(user.getEmail());
		if(user!=null&&user.getUsername().equals(username)) {
			return true;
		}
		return false;
	}

	public String vsView(String vsUsername) {
		String result =userRepository.findByUsername(vsUsername).getSolved();
		return result ;
	}

	public boolean idCheck(String username) {
		if(userRepository.findByUsername(username)!=null) {
			return true;
		}
		return false;
	}
	
	
	
	public void addFavorite(int userId, int boardId) {
		User u = userRepository.findById(userId).orElseThrow(()->{
			return new IllegalArgumentException("아이디가 존재하지 않습니다.");
		});
		boardRepository.findById(boardId).orElseThrow(()->{
			return new IllegalArgumentException("게시글이 존재하지 않습니다.");
		});
		String fa = u.getFavorite();
		for(String X : u.getFavoriteList()) {
			if(boardId==Integer.parseInt(X)) {
				fa =replacePerfect(fa,X);
				u.setFavorite(fa);
				userRepository.save(u);
				return ;
			}
		}
		fa=fa+" "+String.valueOf(boardId);
		u.setFavorite(fa.trim());
		userRepository.save(u);
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

	@Transactional(readOnly=true)
	public List<BoardViewDto>favoriteList(List<String> list) {
		Collections.reverse(list);
		ArrayList<BoardViewDto> arrlist= new ArrayList<>();
		for(String X : list) {
			Board board = boardRepository.findById(Integer.parseInt(X)).orElseThrow(()->{
				return new IllegalArgumentException(X+"번 게시글이 존재하지 않습니다.");
			});
			BoardViewDto bvd = new BoardViewDto(board.getId(), board.getTitle(), board.getContent(), board.getImage(), board.getCount(), null, board.getUser().getId(),board.getUser().getUsername(), board.getCreateDate());
			arrlist.add(bvd);
		}
		return arrlist;
	}
}