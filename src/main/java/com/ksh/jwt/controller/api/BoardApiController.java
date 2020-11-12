package com.ksh.jwt.controller.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.common.ResponseDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.service.BoardService;

@RestController
public class BoardApiController {

	
	@Autowired
	private BoardService boardService;
	
	//글쓰기  ,  
	//문제를 내기 위해서는 먼저 글 작성 후 문제를 작성하는식.
	@PostMapping("/board/save")
	public ResponseDto<String> boardSave(@RequestBody Board board , @AuthenticationPrincipal PrincipalDetails principal){
		board.setCount(0);
		boardService.save(board,principal.getUser());
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
	//인덱스 . 모든 글들을 출력 -> 페이징 완료
	@GetMapping("/index")
	public Page<Board> boardList(@PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<Board> board =boardService.list(pageable);
		return board;
	}
	//검색기능 -> 페이징 아직 안됨 -> 페이징 완료
	@GetMapping("/index/{keyword}")
	public List<Board> boardSearch(@PathVariable String keyword,@PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		List<Board> board =boardService.search(pageable,keyword);
		return board;
	}
	// 글 상세보기 .
	@GetMapping("/board/{id}")
	public Board  view(@PathVariable int id ){
		Board board = boardService.view(id);
		return board;
	}
	//내가 쓴 글 보기
	@GetMapping("mypage/myBoard")
	public List<Board> myBoard(@AuthenticationPrincipal PrincipalDetails principal,@PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		int userId = principal.getUser().getId();
		List<Board> mb = boardService.myBoard(userId,pageable);
		System.out.println(mb.get(0).getTitle());
		return mb;
	}
	
	@DeleteMapping("/board/delete/{id}")
	public ResponseDto<String> deleteBoard(@PathVariable int id){
		
		return new ResponseDto<>(HttpStatus.OK.value(),"1");
	}
}
