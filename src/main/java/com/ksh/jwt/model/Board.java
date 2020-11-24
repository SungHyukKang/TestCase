package com.ksh.jwt.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Board {
	@Id // 해당 테이블의 PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) //시퀀스 AUTO_INCREMENT 
	private int id;

	@Column(nullable = false,length=100) // 널 허용 X , 길이 최대 100
	private String title;
	
	@Lob
	private String content;
	
	private String image;
	
	private int count;
	
	@OneToMany(mappedBy = "board",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE) //1 대 N 관계 , mappedBy를 선언함으로서 연관관계의 주인이 아니다. 즉 테이블의 구성요소가
	@JsonIgnoreProperties({"board"}) // 무한 참조 방지 
	private List<Problem> problems;
	
	@ManyToOne(fetch=FetchType.EAGER) // N대 1관계 .
	@JoinColumn(name="userId") // User의 PK를 Board의 FK로 정해주는 어노테이션
	private User user;
	
	private String username;
	
	@CreationTimestamp//createDate 자동 입력 now();
	private Timestamp createDate;

	@Override
	public String toString() {
		return "Board [id=" + id + ", title=" + title + ", content=" + content + ", image=" + image + ", count=" + count
				+ ", user=" + user + ", username=" + username + ", createDate=" + createDate + "]";
	}
}