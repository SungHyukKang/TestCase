package com.ksh.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksh.jwt.model.Problem;
public interface ProblemRepository extends JpaRepository<Problem, Integer>{
	
	@Modifying
	@Query(value="Insert INTO Problem(title,num1,num2,num3,num4,num5,boardId,answer) values(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery = true) // 네이티브 쿼리를 사용가능
	int write( String title ,String num1 ,String num2 ,String num3,String num4, String num5 ,int boardId,String answer);
}
