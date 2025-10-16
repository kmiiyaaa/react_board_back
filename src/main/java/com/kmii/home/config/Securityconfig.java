package com.kmii.home.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity //모든 요청 url이 스프링 시큐리티의 제어를 받도록 하는 annotation
public class Securityconfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())  // ✅ 새 방식 (람다 DSL)
	        .cors(Customizer.withDefaults())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/signup", "/api/auth/login", "/api/board", 
	            		"/api/board/**", "/api/comments", "/api/comments/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(login -> login
	            .loginProcessingUrl("/api/auth/login")
	            .usernameParameter("username")
	            .passwordParameter("password")
	            .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
	            .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
	        )
	        .logout(logout -> logout
	            .logoutUrl("/api/auth/logout")
	            .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
	        );

	    return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
 // 프론트엔드(React) 주소 허용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React 개발 서버
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 쿠키, 세션 허용 시 필요

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
