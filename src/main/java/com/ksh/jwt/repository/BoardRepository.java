package com.ksh.jwt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ksh.jwt.model.Board;
//Repository는 JPA에서 쿼리문을 사용하는 곳? DB와 소통창구 
public interface BoardRepository extends JpaRepository<Board, Integer>{
	List<Board> findByTitleContaining(String keyword);
}
