package com.ksh.jwt.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.board.BoardPagingViewDto;
import com.ksh.jwt.dto.board.BoardViewDto;
import com.ksh.jwt.dto.board.UpdateBoardDto;
import com.ksh.jwt.dto.common.ResponseDto;
import com.ksh.jwt.model.Board;
import com.ksh.jwt.repository.BoardRepository;
import com.ksh.jwt.service.BoardService;

@RestController
public class BoardApiController {

	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BoardRepository boardRepository;
	
	//글쓰기  ,  
	//문제를 내기 위해서는 먼저 글 작성 후 문제를 작성하는식.
	@PostMapping("/board/save")
	public ResponseDto<Integer> boardSave(@RequestBody Board board , @AuthenticationPrincipal PrincipalDetails principal){
		board.setCount(0);
		board.setUsername(principal.getUsername());
		double num = (Math.random()*10000);
		String imagenum=String.valueOf((int)num%10);
		board.setImage(imagenum);
		boardService.save(board,principal.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(),board.getId());
	}
	
	//인덱스 . 모든 글들을 출력 -> 페이징 완료
	@GetMapping("index")
	public BoardPagingViewDto boardList(@PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<Board> board =boardService.list(pageable);
		
		List<BoardViewDto> list =new ArrayList<>();
		for(Board X :board) {
			BoardViewDto bvd = new BoardViewDto(X.getId(), X.getTitle(), X.getContent(), X.getImage(), X.getCount(), X.getProblems(), X.getUser().getId(),X.getUser().getUsername(), X.getCreateDate());
			list.add(bvd);
		}
		BoardPagingViewDto bpvd = new BoardPagingViewDto(list, pageable,board.getTotalPages(),list.size(),board.getTotalElements());
		return bpvd;
	}
	
	//검색기능 -> 페이징 아직 안됨 -> 페이징 완료//title , username
	@GetMapping("search/{keyword}/{type}")//  http://13.124.124.97:8001/search/{keyword}?type=title or username
	public BoardPagingViewDto boardSearch(@PathVariable String type,@PathVariable String keyword,@PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		List<Board> board =boardService.search(pageable,keyword,type);
		int pageSize=0;
		if(type.equals("title")) {
			pageSize=boardRepository.findByTitleContaining(keyword).size();
		}else {
			pageSize=boardRepository.findByUsernameContaining(keyword).size();
		}
		List<BoardViewDto> list =new ArrayList<>();
		for(Board X :board) {
			BoardViewDto bvd = new BoardViewDto(X.getId(), X.getTitle(), X.getContent(), X.getImage(), X.getCount(), X.getProblems(), X.getUser().getId(),X.getUser().getUsername(), X.getCreateDate());
			list.add(bvd);
		}
		BoardPagingViewDto bpvd = new BoardPagingViewDto(list, pageable,(pageSize/10+1)==1&&list.size()==0 ? 0:(pageSize/10+1),list.size(),pageSize);
		return bpvd;
	}
	
	// 글 상세보기 .
	@GetMapping("board/{id}")
	public BoardViewDto view(@PathVariable int id ){
		BoardViewDto boardView = boardService.view(id);
		return boardView;
	}
	
	//내가 쓴 글 보기
	@GetMapping("mypage/myBoard")
	public List<Board> myBoard(@AuthenticationPrincipal PrincipalDetails principal,@PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		int userId = principal.getUser().getId();
		List<Board> mb = boardService.myBoard(userId,pageable);
		return mb;
	}
	
	//제목 , 내용 , 문제 변경 .또 할게있나?image 변경/추가 , createDate 최신화?
	@PutMapping("board/update/{boardId}")
	public ResponseDto<String> updateBoard(@AuthenticationPrincipal PrincipalDetails principal,@PathVariable int boardId,@RequestBody UpdateBoardDto board){
		System.out.println(board.toString());
		boardService.updateBoard(principal.getUser().getId(),boardId,board);
		return new ResponseDto<>(HttpStatus.OK.value(),"1");
	}
	
	
	@PostMapping("board/delete/{boardId}")
	public ResponseDto<String> deleteBoard(@AuthenticationPrincipal PrincipalDetails principal,@PathVariable int boardId){
		boardService.deleteBoard(principal.getUser().getId(),boardId);
		
		return new ResponseDto<>(HttpStatus.OK.value(),"1");
	}
}
