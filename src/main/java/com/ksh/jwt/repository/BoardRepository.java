package com.ksh.jwt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksh.jwt.model.Board;
//Repository는 JPA에서 쿼리문을 사용하는 곳? DB와 소통창구 
public interface BoardRepository extends JpaRepository<Board, Integer>{
	List<Board> findByTitleContainingOrderById(String keyword ,Pageable pageable );
	List<Board> findByUsernameContainingOrderById(String keyword ,Pageable pageable );
	List<Board> findByUserId(int userId,Pageable pageable);
	List<Board> findByUserId(int userId);
	List<Board> findByUsername(String username , Pageable pageable);
	@Modifying
	@Query(value = "UPDATE Board SET count = count+1 WHERE id =?1",nativeQuery = true)
	int counter(int boardId);
	
}
