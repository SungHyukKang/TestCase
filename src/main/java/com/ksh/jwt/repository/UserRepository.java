package com.ksh.jwt.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ksh.jwt.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	User findByUsername(String username);
}
