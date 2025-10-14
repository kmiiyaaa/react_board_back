package com.kmii.home.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmii.home.entity.Board;
import com.kmii.home.entity.SiteUser;
import com.kmii.home.repository.BoardRepository;
import com.kmii.home.repository.UserRepository;

@RestController
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//전체 게시글 조회
	@GetMapping
	public List<Board> list() {
		return boardRepository.findAll();
	}
	
	//게시글 작성
	@PostMapping
	public ResponseEntity<?> write(@RequestBody Board req, Authentication auth) {
		
		//auth.getName() -> 로그인 유저이름
		
		SiteUser siteUser = userRepository.findByUsername(auth.getName())
				.orElseThrow(() -> new UsernameNotFoundException("사용자 없움"));
		//siteUser -> 현재 로그인한 유저의 레코드
		
		//예전 방식
		Board board = new Board();
		board.setTitle(req.getTitle());  //user가 입력한 글 제목
		board.setContent(req.getContent()); //입력 글 내용
		board.setAuthor(siteUser); //유저정보
		
		boardRepository.save(board);
		
		return ResponseEntity.ok(board);
	}
	
	//특정 게시글 번호(id)로 조회(글 상세보기) , select 조회하는거니까 getmapping
	@GetMapping("/{id}")
	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
//		Board board = boardRepository.findById(id)
//				.orElseThrow(() -> new UsernameNotFoundException("해당글 없움"));
		
		Optional<Board> _board = boardRepository.findById(id);
		if(_board.isPresent()) {
			return ResponseEntity.ok(_board.get()); //해당 id글을 반환 
		}else { //거짓이면 해당글 조회 실패			
			return ResponseEntity.status(404).body("해당 게시글 존재하지 않음");
		}
	}
	
	//특정 id 글 삭제
	@DeleteMapping("/{id}") 
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth){
		
		Optional<Board> _board = boardRepository.findById(id);
		
		//삭제할 글의 존재 여부 확인
		if(_board.isPresent()) {
			return ResponseEntity.status(404).body("해당 게시글은 존재하지 않아 삭제 실패");	
		}
		
		//로그인한 유저의 삭제 권한 확인
		if(auth == null || !auth.getName().equals(_board.get().getAuthor().getUsername())) { //auth.getName:현재 로그인한 id
			return ResponseEntity.status(403).body("해당 게시글에 대한 삭제 권한이 없습니다.");
		}
			boardRepository.delete(_board.get());
			return ResponseEntity.ok("삭제 완료");	
		}
	
	//게시글 수정(권한 설정 -> 로그인 후 본인 작성글만 수정 가능)
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(
			@PathVariable("id") Long id, 
			@RequestBody Board updateBoard, 
			Authentication auth) {
		
		Optional<Board> _board = boardRepository.findById(id);
		
		if(_board.isEmpty()) {
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다.");
		}
		
		if(auth == null || !auth.getName().equals(_board.get().getAuthor().getUsername())) { //auth.getName:현재 로그인한 id
			return ResponseEntity.status(403).body("해당 게시글에 대한 수정 권한이 없습니다.");
		}
		
		Board oldPost = _board.get(); //기존 게시글
		
		oldPost.setTitle(updateBoard.getTitle());
		oldPost.setContent(updateBoard.getContent());
		
		boardRepository.save(oldPost); // 수정한 내용 저장

		return ResponseEntity.ok(_board);
		
	}
	

}
