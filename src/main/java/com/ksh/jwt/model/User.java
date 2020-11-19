package com.ksh.jwt.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;
@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length=100,unique =true) //unique -> username은 다른 튜플이랑 같을 수 없음
	private String username;
	
	@Column(nullable =false ,length=100)
	private String password;
	
	private String roles;
	
	@Column(nullable=false ,length=150,unique=true)
	private String email;

	@CreationTimestamp
	private Timestamp createDate;
	
	@Lob
	private String solved;
	@Lob
	private String wrong;
	
	@Lob
	private String favorite;
	
	public List<String> getFavoriteList(){
		if(this.favorite.length()>0) {
			return Arrays.asList(this.favorite.toString().split(" "));
		}
		return new ArrayList<>();
	}
	
	
	public List<String> getSolvedList(){
		if(this.solved.length()>0) {
			return Arrays.asList(this.solved.toString().split(" "));
		}
		return new ArrayList<>();
	}
	
	public List<String> getWrongList(){
		if(this.wrong.length()>0) {
			return Arrays.asList(this.wrong.toString().split(" "));
		}
		return new ArrayList<>();
	}
	
	public List<String> getRoleList(){
        if(this.roles.toString().length() > 0){
            return Arrays.asList(this.roles.toString().split(","));
        }
        return new ArrayList<>();
    }
	
}
