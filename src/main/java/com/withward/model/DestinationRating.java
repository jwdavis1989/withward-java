package com.withward.model;

// basically a rating
// TODO: 
public class DestinationRating {

	private Integer id;
	private Integer destination_id;
	private Integer user_id;
	private Float rating;
	
	public DestinationRating() {
		super();
	}
	
	public DestinationRating(Integer id, Integer destination_id, Integer user_id, Float rating) {
		this.id = id;
		this.destination_id = destination_id;
		this.user_id = user_id;
		this.rating = rating;
	}
	
	public DestinationRating(Integer destination_id, Integer user_id, Float rating) {
		this.destination_id = destination_id;
		this.user_id = user_id;
		this.rating = rating;
	}

	public Integer getId() {
		return id;
	}

	public Integer getDestination_id() {
		return destination_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}
	
		
}
