package com.ksh.jwt.controller.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ksh.jwt.dto.problem.MySolvedDto;
import com.ksh.jwt.dto.problem.SolvedResponseDto;
import com.ksh.jwt.model.Problem;
import com.ksh.jwt.service.ProblemService;

@RestController
public class ProblemApiController {

	@Autowired // DI 메모리에 띄움
	private ProblemService problemService;
	
	@GetMapping("board/{boardId}/{problemId}")
	public Map<String,String> problemView(@PathVariable int problemId){
		Problem problem =problemService.view(problemId);
		Map<String,String> hsmap =new LinkedHashMap<>();
		hsmap.put("id",String.valueOf(problem.getId()));
		hsmap.put("title",problem.getTitle());
		hsmap.put("num1",problem.getNum1());
		hsmap.put("num2",problem.getNum2());
		hsmap.put("num3",problem.getNum3());
		hsmap.put("num4",problem.getNum4());
		hsmap.put("answer",problem.getAnswer());
		hsmap.put("boardId",String.valueOf(problem.getBoard().getId()));
		return hsmap;
	}
	
	
	// 글 작성 후 문제를 작성.
	@PostMapping("board/{id}/write")
	public ResponseDto<String> write(@PathVariable int id, @RequestBody Map<String,List<Problem>> map,
			@AuthenticationPrincipal PrincipalDetails principal) {
		String key=null;
		for(String X : map.keySet())
			key=X;
		
		System.out.println(map.get(key).toString());
		problemService.write(id,map.get(key), principal.getUser().getUsername());
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}
	
	@DeleteMapping("/problemDelete/{problemId}")
	public ResponseDto<String> delete(@AuthenticationPrincipal PrincipalDetails principal ,@PathVariable int problemId) {
		
		problemService.delete(principal.getUser().getUsername(),problemId);
		return new ResponseDto<String>(HttpStatus.OK.value(), "1");
	}

	@GetMapping("mypage/mySolved")
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
	
	@GetMapping("mypage/myWrong")
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