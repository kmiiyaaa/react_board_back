package com.kmii.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmii.home.entity.Board;
import com.kmii.home.entity.Comment;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	//댓글이 달린 원 게시글로 댓글 리스트 반환 메서드
	List<Comment> findByBoard(Board board);

}
