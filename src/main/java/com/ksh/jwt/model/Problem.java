package com.ksh.jwt.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false,length=100)
	private String title;
	
	@Column(nullable=false)
	private String num1;
	@Column(nullable=false)
	private String num2; 
	@Column(nullable=false)
	private String num3;
	@Column(nullable=false)
	private String num4;
//	@Column(nullable=false)
//	private String num5;
	
	@ManyToOne(fetch=FetchType.LAZY)//하나의 게시글은 여러개의 문제를 가지고 있을 수 있다.
	@JoinColumn(name="boardId",nullable = false)
	@JsonBackReference
	private Board board;
	
	@Column(nullable=false)
	private String answer;

	@Override
	public String toString() {
		return "Problem [id=" + id + ", title=" + title + ", num1=" + num1 + ", num2=" + num2 + ", num3=" + num3
				+ ", num4=" + num4 + ",  answer=" + answer + "]";
	}
	

}