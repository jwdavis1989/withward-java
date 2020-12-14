package com.withward.model;

public class User {
	private Integer id;
	private String username;
	private String email;
	private String password;
	private String photo;
	
	public User () {
		super();
	}
	
	public User(Integer id, String username, String email, String password, String photo) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.photo = photo;
	}
	
	public User(String username, String email, String password, String photo) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.photo = photo;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(Integer id, String username, String photo) {
		this.id = id;
		this.username = username;
		this.photo = photo;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhoto() {
		return this.photo;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
