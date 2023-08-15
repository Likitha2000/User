package com.something.app.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/user")
public class RegistrationController {

	    @Autowired
	    private UserRepository userRepository;

	    @PostMapping("/register")
	    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
	        if (result.hasErrors()) {
	            Map<String, String> errorMap = new HashMap<>();
	            for (FieldError error : result.getFieldErrors()) {
	                errorMap.put(error.getField(), error.getDefaultMessage());
	            }
	            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	        }
	        Optional<User> returnedUser = Optional.ofNullable(userRepository.findByNameAndPassword(user.getName(),user.getPassword()));
	        if(returnedUser.isPresent()) {
	        	return new ResponseEntity<>("Failure", HttpStatus.NOT_ACCEPTABLE);
	        }	

	        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> loginDetails){
	    	Optional<User> returnedUser = Optional.ofNullable(userRepository.findByNameAndPassword(loginDetails.get("name"), loginDetails.get("password")));
	    	if(returnedUser!=null){ 
	    		 return new ResponseEntity<>(returnedUser.get().getId(), HttpStatus.OK);
	    	}
	    	return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
	    }
	    
	    
	    @GetMapping("/all")
	    public ResponseEntity<?> displayAll(){
	    	return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
	    }
}