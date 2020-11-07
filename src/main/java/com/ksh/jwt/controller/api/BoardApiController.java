package com.ksh.jwt.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.ResponseDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.service.BoardService;

@RestController
public class BoardApiController {

	
	@Autowired
	private BoardService boardService;
	
	//글쓰기  ,  
	//문제를 내기위해서는 먼저 글 작성 후 문제를 작성하는식.
	@PostMapping("/board/save")
	public ResponseDto<String> boardSave(@RequestBody Board board , @AuthenticationPrincipal PrincipalDetails principal){
		boardService.save(board,principal.getUser());
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
	//인덱스 . 모든 글들을 출력
	@GetMapping("/index")
	public ResponseDto<Page<Board>> boardList(@PageableDefault(size=3,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<Board> board =boardService.list(pageable);
		return new ResponseDto<Page<Board>>(HttpStatus.OK.value(),board);
	}
	//검색기능 -> 페이징 아직 안됨
	@GetMapping("/index/{keyword}")
	public ResponseDto<List<Board>> boardSearch(@PathVariable String keyword,@PageableDefault(size=3,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		List<Board> board =boardService.search(pageable,keyword);
		return new ResponseDto<List<Board>>(HttpStatus.OK.value(),board);
	}
	// 글 상세보기 .
	@GetMapping("/board/{id}")
	public ResponseDto<Board> view(@PathVariable int id ){
		Board board = boardService.view(id);
		return new ResponseDto<Board>(HttpStatus.OK.value(),board);
	}
}
