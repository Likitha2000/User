package com.something.app.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.something.app.user.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>{


	List<Answer> findByUserId(Long userId);
}

