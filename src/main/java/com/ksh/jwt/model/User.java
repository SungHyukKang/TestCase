package com.ksh.jwt.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
	
	public List<String> getRoleList(){
        if(this.roles.toString().length() > 0){
            return Arrays.asList(this.roles.toString().split(","));
        }
        return new ArrayList<>();
    }
	
	@CreationTimestamp
	private Timestamp createDate;
	
}
