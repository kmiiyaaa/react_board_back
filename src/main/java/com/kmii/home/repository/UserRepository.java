package com.kmii.home.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmii.home.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	
	public Optional<SiteUser> findByUsername(String username); //Optional -> null값 방지를 위해서
}
