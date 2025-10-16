package com.kmii.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmii.home.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	

}
