package com.ksh.jwt.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksh.jwt.dto.ResponseDto;
import com.ksh.jwt.model.Problem;
import com.ksh.jwt.service.ProblemService;

@RestController
public class ProblemApiController {

	@Autowired //DI 메모리에 띄움
	private ProblemService problemService;
	
	//글 작성 후 문제를 작성.
	@PostMapping("/board/{id}/write")
	public ResponseDto<String> write(@PathVariable int id,@RequestBody Problem problem){
		problemService.write(id,problem);
		return new ResponseDto<String>(HttpStatus.OK.value(),"1");
	}
	
}
