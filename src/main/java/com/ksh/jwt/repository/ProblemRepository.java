package com.ksh.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksh.jwt.model.Problem;
public interface ProblemRepository extends JpaRepository<Problem, Integer>{
	
	List<Problem> findByBoardId(int boardId); 
	
	@Modifying
	@Query(value="Insert INTO Problem(title,num1,num2,num3,num4,boardId,answer) values(?1,?2,?3,?4,?5,?6,?7)",nativeQuery = true) // 네이티브 쿼리를 사용가능
	int write( String title ,String num1 ,String num2 ,String num3,String num4,int boardId,String answer);
}
