package com.something.app.user.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.something.app.user.model.Answer;
import com.something.app.user.model.Question;
import com.something.app.user.model.ResponseBody;
import com.something.app.user.model.QuestionResponse;
import com.something.app.user.repository.AnswerRepository;
import com.something.app.user.repository.QuestionRepository;
import com.something.app.user.repository.UserRepository;


@Service
public class UserService {

	@Autowired
	private AnswerRepository answerRepo;
	
	@Autowired
	private QuestionRepository questionRepo;

	public ResponseBody fetchQuestionAndAnswers(Long userId) {
		// TODO Auto-generated method stub
		ResponseBody responseBody = new ResponseBody();
		try {
		List<Answer> answers = answerRepo.findByUserId(userId);
		List<Question> questions = questionRepo.findAll();
		List<QuestionResponse> questionResponses = new ArrayList<QuestionResponse>(); 
		for(int i=0;i<questions.size();i++) {
			QuestionResponse questionResponse = new QuestionResponse();
			Long questionId = questions.get(i).getId();
			List<Answer> finalAnswer = answers.stream().filter(answer -> answer.getUserId().equals(questionId)).collect(Collectors.toList());
			questionResponse.setId(questions.get(i).getId());
			questionResponse.setQuestion(questions.get(i).getQuestion());
			questionResponse.setAnswerType(questions.get(i).getAnswerType());
			questionResponse.setAnswer(finalAnswer);
			questionResponses.add(questionResponse);
			
		}
	
		responseBody.setQuestionResponses(questionResponses);
		responseBody.setMessage("Success");
		responseBody.setStatus(1);
		}
		catch(Exception e) {
			responseBody.setQuestionResponses(null);
			responseBody.setMessage("Failure");
			responseBody.setStatus(0);
		}
		return responseBody;
		
	}
	
	
}
