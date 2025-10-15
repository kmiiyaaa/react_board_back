package com.kmii.home.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmii.home.dto.SiteUserDto;
import com.kmii.home.entity.SiteUser;
import com.kmii.home.repository.UserRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@PostMapping("/signup") // sql -> insert -> post
//	public ResponseEntity<?> signup(@RequestBody SiteUser req) { // <?> 해놓으면 문자든 객체든 다 들어올 수 있다.
//		//사용자 이름(username)이 DB에 이미 존재하는지 확인
//		if(userRepository.findByUsername(req.getUsername()).isPresent()) {
//			//참이면->이미지 해당 username(아이디)가 존재하므로 가입 불가
//			//거짓이면->가입 가능
//			return ResponseEntity.badRequest().body("이미 존재하는 사용자명 입니다."); //가입실패 -> 에러 메시지
//		}
//		req.setPassword(passwordEncoder.encode(req.getPassword())); 
//		//비밀번호 암호화해서 엔티티에 다시 넣기
//		userRepository.save(req);
//		
//		return ResponseEntity.ok("회원가입 성공!"); //가입성공 -> 성공메시지
//		//return ResponseEntity.ok(req); //가입성공 후 해당 엔티티 반환
//	}
//	
	
	
	//validation 적용 회원가입 (빈칸 입력 방지, 최소 4글자 이상)
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SiteUserDto siteUserDto, BindingResult bindingResult) { //siteUser라는 엔티티티가 직접 받는게아니라 dto가 대신 받는다. (위 원본과 비교)
		
		//spring validation 결과 처리
		if(bindingResult.hasErrors()) { //참이면 유효성 체크 실패 -> err
			//restful api라서 다른데로 돌려보내는거 안해줘도된다. 에러만 표시해주면 된다.
			
			Map<String, String> errors = new HashMap<>();  // username, password필드가 String, message가 String이라서 <String,String>
			bindingResult.getFieldErrors().forEach(  //에러의 갯수는 여러개일 확률이 높아서 getfielderrors로 넣는다.
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
						// 회원 가입시 -> username:abc, password:123 시 에러나면
						// {
						//"username":"아이디는 최소 4글자 이상입니다.", 
						//"password":"비밀번호는 최소 4글자 이상입니다."
						// }
					}
				);		
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); // 에러나면 위에 적은 {~~} 값이 반환된다
			
			}
		
		SiteUser siteUser = new SiteUser();  //Entity 객체 선언
		//사용자가 입력한 username(dto에 존재)을 entity객체에 set
		siteUser.setUsername(siteUserDto.getUsername());
		//사용자가 입력한 password(dto에 존재)를 entity객체에 set
		siteUser.setPassword(siteUserDto.getPassword());
		
		//사용자 이름(username)이 DB에 이미 존재하는지 확인
		if(userRepository.findByUsername(siteUser.getUsername()).isPresent()) {
			//참이면->이미지 해당 username(아이디)가 존재하므로 가입 불가
			//거짓이면->가입 가능
			return ResponseEntity.badRequest().body("이미 존재하는 사용자명 입니다."); //가입실패 -> 에러 메시지
		}
		siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword())); 
		//비밀번호 암호화해서 엔티티에 다시 넣기
		userRepository.save(siteUser);
		
		return ResponseEntity.ok("회원가입 성공!"); //가입성공 -> 성공메시지
		//return ResponseEntity.ok(req); //가입성공 후 해당 엔티티 반환
	}
	
	@GetMapping("/me") //현재 로그인한 사용자 정보를 가져오는 요청(나 자신의 정보->현재 로그인한 유저의 정보)
	public ResponseEntity<?> me(Authentication auth) {
		return ResponseEntity.ok(Map.of("username", auth.getName()));
	}

}
