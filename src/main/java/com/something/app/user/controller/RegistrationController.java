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

import com.something.app.user.model.User;
import com.something.app.user.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/user")
public class RegistrationController {

	    @Autowired
	    private UserRepository userRepository;
	    
	    @Value("${app.jwt.secret}")
	    private String jwtSecret;

	    @Value("${app.jwt.expiration}")
	    private int jwtExpiration;


	    @PostMapping("/register")
	    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
	    	try {
	        if (result.hasErrors()) {
	            Map<String, String> errorMap = new HashMap<>();
	            for (FieldError error : result.getFieldErrors()) {
	                errorMap.put(error.getField(), error.getDefaultMessage());
	            }
	            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	        }
	        User returnedUser = userRepository.findByEmailAndPassword(user.getEmail(),user.getPassword());
	        if(returnedUser!=null) {
	        	return new ResponseEntity<>("Failure", HttpStatus.NOT_ACCEPTABLE);
	        }	

	        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
	    	}
	    catch(Exception e) {
	    	return new ResponseEntity<>("Server Error",HttpStatus.NOT_FOUND);	    
	    }
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> loginDetails){
	    	try {
	    		User returnedUser = userRepository.findByEmailAndPassword(loginDetails.get("email"), loginDetails.get("password"));
		    	if(returnedUser == null) {
		    		return new ResponseEntity<>("Password Incorrect", HttpStatus.NOT_FOUND);
		    	}
		    	else{ 
			    	String token = generateToken(returnedUser.getEmail());
			    	HttpHeaders headers = new HttpHeaders();
			    	headers.add("token", token);
		    		 return ResponseEntity.status(HttpStatus.OK).headers(headers).body(returnedUser);
		    	}
	    	}
	    	catch(Exception e) {
	    	return new ResponseEntity<>("Server Error", HttpStatus.NOT_FOUND);
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
	    
	    @PostMapping("/getQuestions")
	    public ResponseEntity<?> getAllQuestions(){
	    	return null;
	    }
}

