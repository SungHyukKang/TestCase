package com.ksh.jwt.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksh.jwt.dto.board.BoardViewDto;
import com.ksh.jwt.dto.board.UpdateBoardDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.model.Problem;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.repository.ProblemRepository;
import com.ksh.jwt.repository.UserRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Transactional
	public void save(Board board, User user) {
		board.setUser(user);

		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public Page<Board> list(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional
	public BoardViewDto view(int id) {
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글을 읽어올 수 없습니다.");
		});
		boardRepository.counter(id);
		BoardViewDto bvd = new BoardViewDto(board.getId(), board.getTitle(), board.getContent(), board.getImage(),
				board.getCount(), board.getProblems(), board.getUser().getId(), board.getUser().getUsername(),
				board.getCreateDate());
		return bvd;
	}

	@Transactional(readOnly = true)
	public List<Board> search(Pageable pageable, String keyword, String type) {
		List<Board> list;
		if (type.equals("title")) {
			list = boardRepository.findByTitleContaining(keyword, pageable);
			System.out.println(list);
			return list;
		} else {
			list = boardRepository.findByUsernameContaining(keyword, pageable);
			System.out.println(list);
			return list;
		}
	}

	@Transactional(readOnly = true)
	public List<Board> myBoard(int userId, Pageable pageable) {
		List<Board> mb = boardRepository.findByUserId(userId, pageable);
		return mb;
	}

	@Transactional
	public void updateBoard(int id, int boardId, UpdateBoardDto ubd) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			return new IllegalArgumentException("게시글을 찾을 수 없습니다.");
		});
		if (board.getUser().getId() != id) {
			throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
		} else {
			board.setTitle(ubd.getTitle());
			board.setContent(ubd.getContent());
			List<Problem> list = problemRepository.findByBoardId(boardId);
			Collections.sort(list,(args0,args1)->{
				return args0.getId()>=args1.getId() ? -1:1;
			});
			Collections.sort(ubd.getProblems(),(args0,args1)->{
				return args0.getId()>=args1.getId() ? -1:1;
			});
			int o =0;
			for(Problem p: list) {
				Problem pro = problemRepository.findById(p.getId()).orElseThrow(()->{
					return new IllegalArgumentException("문제를 찾을 수 없습니다.");
				});
				for(Problem pp : ubd.getProblems()) {
					if(pp.getId()==pro.getId()) {
						pro.setAnswer(pp.getAnswer());
						pro.setNum1(pp.getNum1());
						pro.setNum2(pp.getNum2());
						pro.setNum3(pp.getNum3());
						pro.setNum4(pp.getNum4());
						pro.setTitle(pp.getTitle());
						o++;
						break;
					}
				}
			}
			if(list.size()<ubd.getProblems().size()) {
				for(int i =o;i<ubd.getProblems().size();i++) {
					ubd.getProblems().get(i).setBoard(board);
					list.add(ubd.getProblems().get(i));
				}
			}
			
			problemRepository.saveAll(list);
			board.setProblems(ubd.getProblems());
			System.out.println(board.getProblems());
			System.out.println(board.toString());
		}
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
	
	@Transactional
	public void deleteBoard(int id, int boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			return new IllegalArgumentException("게시글을 찾을 수 없습니다.");
		});
		List<User> users =userRepository.findAll();
		HashMap<Integer,Boolean> hsmap =new HashMap<>();
		HashMap<Integer,Boolean> boardHashmap=new HashMap<>();
		boardHashmap.put(boardId,true);
		if (board.getUser().getId() != id) {
			throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
		} else {
			for (Problem p : board.getProblems()) {
				hsmap.put(p.getId(), true);
				problemRepository.delete(p);
			}
			for(User u : users) {
				if(u==null)
					continue;
				String sol = u.getSolved();
				String wro = u.getWrong();
				String fav = u.getFavorite();
				for(String p : u.getSolvedList()) {
					if(hsmap.get(Integer.parseInt(p))!=null&&hsmap.get(Integer.parseInt(p)))
						sol =replacePerfect(sol,p);
				}
				for(String w : u.getWrongList()) {
					if(hsmap.get(Integer.parseInt(w))!=null&&hsmap.get(Integer.parseInt(w)))
						wro =replacePerfect(wro,w);
				}
				for(String f : u.getFavoriteList()) {
					if(boardHashmap.get(Integer.parseInt(f))!=null&&boardHashmap.get(Integer.parseInt(f)))
						fav=replacePerfect(fav,f);
				}
				u.setSolved(sol);
				u.setFavorite(fav);
				u.setWrong(wro);
			}
			boardRepository.delete(board);
		}
	}
}
