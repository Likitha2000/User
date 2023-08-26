package com.something.app.user.model;

public class ResponseBody {
	
	private int status;
	private String message;
	private UserDataResponse userData;
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
	
}
