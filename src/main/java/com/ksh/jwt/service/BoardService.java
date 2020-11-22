package com.ksh.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksh.jwt.dto.board.BoardViewDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.model.Problem;
import com.ksh.jwt.model.User;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.repository.ProblemRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

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
	public void updateBoard(int id, int boardId, Board ubd) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			return new IllegalArgumentException("게시글을 찾을 수 없습니다.");
		});
		if (board.getUser().getId() != id) {
			throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
		} else {
			board.setTitle(ubd.getContent());
			board.setContent(ubd.getContent());
			board.setImage(ubd.getImage());
			board.setProblems(ubd.getProblems());
		}
	}

	@Transactional
	public void deleteBoard(int id, int boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			return new IllegalArgumentException("게시글을 찾을 수 없습니다.");
		});
		if (board.getUser().getId() != id) {
			throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
		} else {
			for (Problem p : board.getProblems()) {
				System.out.println(p.getId());
				problemRepository.delete(p);
				System.out.println("!!");
			}
			boardRepository.delete(board);
		}
	}
}
