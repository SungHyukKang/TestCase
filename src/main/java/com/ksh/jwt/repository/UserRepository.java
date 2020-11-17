package com.ksh.jwt.repository;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ksh.jwt.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	User findByUsername(String username);
	User findByEmail(String email);
	
	List<User> findByUsernameContainingOrderById(String keyword ,Pageable pageable);
	
	void deleteByUsername(String username);
	
	@Modifying
	@Query(value="UPDATE User SET password=?2 WHERE id=?1" , nativeQuery=true)
	void updateUserPassword(int id, String pw);
	
}
