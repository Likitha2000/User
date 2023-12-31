package com.something.app.user.controller;

import javax.crypto.SecretKey;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.something.app.user.model.Answer;
import com.something.app.user.model.Question;
import com.something.app.user.model.ResponseBody;
import com.something.app.user.model.User;
import com.something.app.user.repository.AnswerRepository;
import com.something.app.user.repository.QuestionRepository;
import com.something.app.user.repository.UserRepository;
import com.something.app.user.service.UserService;
import com.something.app.user.model.UserDataResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/user")
public class RegistrationController {

	    @Autowired
	    private UserRepository userRepository;
	    
	    @Autowired
	    private QuestionRepository questionRepo;
	    
	    @Autowired
	    private AnswerRepository answerRepo;
	    
	    @Autowired
	    private UserService userService;

	    @Value("${app.jwt.expiration}")
	    private int jwtExpiration;


	    @PostMapping("/register")
	    public ResponseBody registerUser(@Valid @RequestBody User user, BindingResult result) {
	    	ResponseBody responseBody = new ResponseBody();
	    	try {
	        if (result.hasErrors()) {
	            Map<String, String> errorMap = new HashMap<>();
	            for (FieldError error : result.getFieldErrors()) {
	                errorMap.put(error.getField(), error.getDefaultMessage());
	            }
	            responseBody.setStatus(0);
	            responseBody.setMessage("Error in data passed");
	            responseBody.setUserData(null);
	            return responseBody;
	        }
	        User returnedUser = userRepository.findByEmailAndPassword(user.getEmail(),user.getPassword());
	        if(returnedUser!=null) {
	        	responseBody.setStatus(0);
	            responseBody.setMessage("User already exists");
	            responseBody.setUserData(null);
	            return responseBody;
	        }	
	        else {
		        User finalUser = userRepository.save(user);
		        responseBody.setStatus(1);
		        responseBody.setMessage("Success");
		        UserDataResponse userData = new UserDataResponse();
		        userData.setId(finalUser.getId());
		        userData.setEmail(finalUser.getEmail());
		        userData.setDob(finalUser.getDob());
		        userData.setName(finalUser.getName());
		        userData.setPhoneNumber(finalUser.getPhoneNumber());
		        userData.setRoleType(finalUser.getRoleType());
		        responseBody.setUserData(userData);
		        return responseBody;
	        }

	    	}
	    catch(Exception e) {
	    	responseBody.setStatus(0);
            responseBody.setMessage("Server Error");
            responseBody.setUserData(null);
            return responseBody;	    
	    }
	    }
	    
	    @PostMapping("/login")
	    public ResponseBody loginUser(@RequestBody Map<String,String> loginDetails){
	    	try {
	    		User returnedUser = userRepository.findByEmail(loginDetails.get("email"));
		    	ResponseBody responseBody = new ResponseBody();
	    		if(returnedUser == null) {
		    		responseBody.setStatus(0);
		            responseBody.setMessage("User not found");
		            responseBody.setUserData(null);
		            return responseBody;
		    	}
	    		else if(userRepository.findByEmailAndPassword(loginDetails.get("email"), loginDetails.get("password")) == null) {
	    			responseBody.setStatus(0);
		            responseBody.setMessage("Password incorrect");
		            responseBody.setUserData(null);
		            return responseBody;
	    		}
		    	else{ 
			    	String token = generateToken(returnedUser.getEmail());
			    	responseBody.setStatus(1);
			    	responseBody.setMessage("Success");
			    	UserDataResponse userData = new UserDataResponse();
			    	userData.setToken(token);
			    	userData.setName(returnedUser.getName());
			    	userData.setEmail(returnedUser.getEmail());
			    	userData.setDob(returnedUser.getDob());
			    	userData.setPhoneNumber(returnedUser.getPhoneNumber());
			    	userData.setRoleType(returnedUser.getRoleType());
			    	responseBody.setUserData(userData);
			    	return responseBody;
		    	}
	    	}
	    	catch(Exception e) {
	    		ResponseBody responseBody = new ResponseBody();
	    		responseBody.setStatus(0);
		    	responseBody.setMessage("Server Error");
		    	responseBody.setUserData(null);
		    	return responseBody;
	    	}
	    }
	    
	    private String generateToken(String username) {
	        Date now = new Date();
	        Date expirationDate = new Date(now.getTime() + jwtExpiration * 1000);
	        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(now)
	                .setExpiration(expirationDate)
	                .signWith(SignatureAlgorithm.HS512, key)
	                .compact();
	    }
	    
	    @GetMapping("/all")
	    public ResponseEntity<?> displayAll(){
	    	return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
	    }
	    
	    @GetMapping("/getQuestions")
	    public List<Question> getAllQuestions(){
	    	return questionRepo.findAll();
	    }
	    
	    @PostMapping("/insertQuestion")
	    public Question insertQuestion(@RequestBody Question question) {
	    	return questionRepo.save(question);
	    }
	    
	    @GetMapping("/getAnswers")
	    public List<Answer> submitAnswers() {
//	    	return userRespository.save
	    	return answerRepo.findAll();
	    }
	    
	    @PostMapping("/postAnswers")
	    public ResponseBody postAnswers(@RequestBody com.something.app.user.model.RequestBody requestBody) {
	    	ResponseBody responseBody = new ResponseBody();
	    	try {
	    		responseBody.setStatus(1);
	    		responseBody.setMessage("Success");
	    		responseBody.setAnswerResponses(answerRepo.saveAll(requestBody.getAnswer()));
	    	}
	    	catch(Exception e) {
	    		responseBody.setStatus(0);
	    		responseBody.setMessage("Failure");
	    		responseBody.setAnswerResponses(null);   		
	    		}
	  
	    	return responseBody;
	    }
	    @PostMapping("/fetchQuestionAnswers")
	    public ResponseBody fetchQuestionsAndAnswers(@RequestBody com.something.app.user.model.RequestBody requestBody) {
	    	Long userId = requestBody.getUserId();
	    	ResponseBody response = userService.fetchQuestionAndAnswers(userId);
	    	return response;
	    }
}

