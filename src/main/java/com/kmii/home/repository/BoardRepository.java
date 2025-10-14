package com.kmii.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmii.home.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
	

}
