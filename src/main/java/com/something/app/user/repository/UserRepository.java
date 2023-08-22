package com.something.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.something.app.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
//	User findByNameAndPassword(String name, String password);

	User findByEmailAndPassword(String email, String password);

	Optional<User> findByEmail(String email);
}
