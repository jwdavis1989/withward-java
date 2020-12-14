package com.withward.DTO;

import java.util.ArrayList;

import com.withward.model.DestinationRating;

/**
 * Template Class that represents a Destination and that has multiple ratings
 */
public class DestinationDTO {

	private Integer id;
	private Integer withlist_id;
	private String name;
	private String description;
	private String photo;
	private boolean completed;
	private Float averageRating;
	private ArrayList<DestinationRating> ratings;

	public DestinationDTO() {
		super();
	}

	public DestinationDTO(Integer id, Integer withlist_id, String name, String description, String photo,
			boolean completed, Float averageRating, ArrayList<DestinationRating> ratings) {
		this.id = id;
		this.withlist_id = withlist_id;
		this.name = name;
		this.description = description;
		this.photo = photo;
		this.completed = completed;
		this.averageRating = averageRating;
		this.ratings = ratings;

	}

	public DestinationDTO(Integer withlist_id, String name, String description, String photo, boolean completed,
			Float averageRating, ArrayList<DestinationRating> ratings) {
		this.withlist_id = withlist_id;
		this.name = name;
		this.description = description;
		this.photo = photo;
		this.completed = completed;
		this.averageRating = averageRating;
		this.ratings = ratings;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAvergaeRating(Float averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getId() {
		return id;
	}

	public Integer getWithlist_id() {
		return withlist_id;
	}

	public ArrayList<DestinationRating> getRatings() {
		return this.ratings;
	}

}
