package com.ksh.jwt.controller.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.config.auth.PrincipalDetails;
import com.ksh.jwt.dto.common.ResponseDto;
import com.ksh.jwt.dto.problem.MySolvedDto;
import com.ksh.jwt.dto.problem.SolvedResponseDto;
import com.ksh.jwt.model.Problem;
import com.ksh.jwt.service.ProblemService;

@RestController
public class ProblemApiController {

	@Autowired // DI 메모리에 띄움
	private ProblemService problemService;

	// 글 작성 후 문제를 작성.
	@PostMapping("/board/{id}/write")
	public ResponseDto<String> write(@PathVariable int id, @RequestBody Problem[] problems,
			@AuthenticationPrincipal PrincipalDetails principal) {
		problemService.write(id, problems, principal.getUser().getUsername());
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	@GetMapping("/mypage/mySolved")
	public List<SolvedResponseDto> mySolved(@AuthenticationPrincipal PrincipalDetails principal) {
		HashMap<Integer, List<MySolvedDto>> hsmap = problemService.mySolved(principal.getUser().getSolvedList());
		List<SolvedResponseDto> list = new ArrayList<>();
		for (int boardId : hsmap.keySet()) {
			List<MySolvedDto> msd = hsmap.get(boardId);
			Collections.sort(msd, (arg0, arg1) -> {
				return arg0.getProblemId() >= arg1.getProblemId() ? 1 : -1;
			});
			list.add(new SolvedResponseDto(boardId, msd));
		}
		Collections.sort(list, (arg0, arg1) -> {
			return arg0.getBoardId() >= arg1.getBoardId() ? 1 : -1;
		});
		return list;
	}
	
	@GetMapping("/mypage/myWrong")
	public List<SolvedResponseDto> myWrong(@AuthenticationPrincipal PrincipalDetails principal) {
		HashMap<Integer, List<MySolvedDto>> hsmap = problemService.mySolved(principal.getUser().getWrongList());
		List<SolvedResponseDto> list = new ArrayList<>();
		for (int boardId : hsmap.keySet()) {
			List<MySolvedDto> msd = hsmap.get(boardId);
			Collections.sort(msd, (arg0, arg1) -> {
				return arg0.getProblemId() >= arg1.getProblemId() ? 1 : -1;
			});
			list.add(new SolvedResponseDto(boardId, msd));
		}
		Collections.sort(list, (arg0, arg1) -> {
			return arg0.getBoardId() >= arg1.getBoardId() ? 1 : -1;
		});
		return list;
	}
}