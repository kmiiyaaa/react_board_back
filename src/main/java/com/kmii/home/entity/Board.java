package com.kmii.home.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.kmii.home.entity.SiteUser;
import com.kmii.home.entity.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //게시판 번호
	
	private String title; //게시판 제목	
	private String content; //게시판 내용
	
	@CreationTimestamp //자동으로 insert 시 현재 날짜시간 삽입
	private LocalDateTime createDate; //게시판 글쓴 날짜
	
	@ManyToOne //N:1 관계->게시판글:유저
	private SiteUser author; //게시판 글쓴이
	
	
	// 댓글있는 게시글 삭제 안됨 후 설정
	
	// 게시글(1) : 댓글들(N) -> 글과 연관되어있는 댓글들 삭제설정
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<Comment> comments = new ArrayList<>();
	

}
