package com.withward.DTO;

/**
 * Template class that represents users without email and password
 */
public class UserDTO {
	private Integer id;
	private String username;
	private String email;
	private String photo;
	private Boolean isAdmin = false;

	public UserDTO() {
		super();
	}

	public UserDTO(Integer id, String username, String email, String photo, Boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.photo = photo;
		this.isAdmin = isAdmin;
	}

	public UserDTO(Integer id, String username, String photo) {
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

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
