package com.ksh.jwt.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksh.jwt.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	User findByUsername(String username);
	User findByEmail(String email);
	
	@Modifying
	@Query(value="UPDATE User SET password=?2 WHERE id=?1" , nativeQuery=true)
	void updateUserPassword(int id, String pw);
	
}
