package com.withward.model;

public class Withlist {

	private Integer id;
	private Integer ownerId;
	private String title;
	private String description;

	public Withlist() {
		super();
	}
	
	public Withlist(Integer id, Integer ownerId, String title, String description) {
		this.id = id;
		this.ownerId = ownerId;
		this.title = title;
		this.description = description;
	}
	
	public Withlist(Integer ownerId, String title, String description) {
		this.ownerId = ownerId;
		this.title = title;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public Integer getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
