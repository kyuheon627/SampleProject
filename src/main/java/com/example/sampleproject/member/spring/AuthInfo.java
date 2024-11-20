package com.example.sampleproject.member.spring;

public class AuthInfo {

	private Long id;
	private String email;
	private String name;
	private String role;

	public AuthInfo(Long id, String email, String name, String role) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getRole() {return role;}
	
	
	
}
