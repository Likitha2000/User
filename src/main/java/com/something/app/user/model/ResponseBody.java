package com.something.app.user.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody {
	
	private int status;
	private String message;
	private UserDataResponse userData;
	private List<QuestionResponse> questionResponses;
	private List<Answer> answerResponses;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserDataResponse getUserData() {
		return userData;
	}
	public void setUserData(UserDataResponse userData) {
		this.userData = userData;
	}
	public List<QuestionResponse> getQuestionResponses() {
		return questionResponses;
	}
	public void setQuestionResponses(List<QuestionResponse> questionResponses) {
		this.questionResponses = questionResponses;
	}
	public List<Answer> getAnswerResponses() {
		return answerResponses;
	}
	public void setAnswerResponses(List<Answer> answerResponses) {
		this.answerResponses = answerResponses;
	}
	
}
