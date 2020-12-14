package com.withward.model;

public class WithlistUser {
	private Integer id;
	private Integer withlist_id;
	private Integer user_id;
	
	public WithlistUser () {
		super();
	}
	
	public WithlistUser(Integer id, Integer withlist_id, Integer user_id) {
		this.id = id;
		this.withlist_id = withlist_id;
		this.user_id = user_id;
	}
	
	public WithlistUser(Integer withlist_id, Integer user_id) {
		this.withlist_id = withlist_id;
		this.user_id = user_id;
	}

	public Integer getWithlist_id() {
		return withlist_id;
	}

	public void setWithlist_id(Integer withlist_id) {
		this.withlist_id = withlist_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getId() {
		return id;
	}
	
}
