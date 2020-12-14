package com.withward.DTO;

import java.util.ArrayList;

import com.withward.model.Destination;

/**
 * Template Class that represents a withlist that has destinations and users.
 */
public class WithlistDTO {
	private Integer id;
	private Integer ownerId;
	private String title;
	private String description;
	private ArrayList<Destination> destinations;
	private ArrayList<UserDTO> users;

	public WithlistDTO() {
		super();
	}

	public WithlistDTO(Integer id, Integer ownerId, String title, String description,
			ArrayList<Destination> destinations, ArrayList<UserDTO> users) {
		this.id = id;
		this.ownerId = ownerId;
		this.title = title;
		this.description = description;
		this.destinations = destinations;
		this.users = users;
	}

	public WithlistDTO(Integer ownerId, String title, String description, ArrayList<Destination> destinations,
			ArrayList<UserDTO> users) {
		this.ownerId = ownerId;
		this.title = title;
		this.description = description;
		this.destinations = destinations;
		this.users = users;
	}

	public Integer getId() {
		return this.id;
	}

	public Integer getOwnerId() {
		return this.ownerId;
	}

	public ArrayList<Destination> getDestinations() {
		return this.destinations;
	}

	public ArrayList<UserDTO> getUsers() {
		return this.users;
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
