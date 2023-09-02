package com.something.app.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.something.app.user.model.Question;

public interface QuestionRepository extends JpaRepository<Question,Long>{

	
}