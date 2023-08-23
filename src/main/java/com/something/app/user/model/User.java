package com.something.app.user.model;

import jakarta.persistence.*;

import java.util.Collection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    @Column
    private String name;
    
    @NotBlank
    @Email
    @Column
    private String email;
    
    @NotBlank
    @Size(min = 8)
    @Column
    private String password;
    
    @NotBlank
    @Column
    private String gender;
    
    @NotBlank
    @Column
    private String dob;
    
    @NotBlank
    @Size(max=10)
    @Column
    private String phoneNumber;
    
    @NotBlank
    @Column
    private int roleType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}


}
