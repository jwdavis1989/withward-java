package com.withward.service;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.withward.DTO.UserDTO;
import com.withward.model.User;
import com.withward.repository.UserDAO;

public class UserService {

	private UserDAO userRepository = new UserDAO();
	
	public ArrayList<UserDTO> getAllUsers() throws SQLException{
		return userRepository.getAll();
	}
	
	public boolean isAuthenticated(String username, String password) throws SQLException{
		return userRepository.authenticateUser(username, password);
	}
	
	public boolean isAdmin(String username) throws SQLException {
		return userRepository.authAdmin(username);
	}
	
	public UserDTO getOneUser(Integer userId)throws SQLException {
		return userRepository.getUser(userId);
	}
	
	public UserDTO getByUsername(String username) throws SQLException {
		return userRepository.getUserByUsername(username);
	}
	
	public UserDTO createUser(User user)throws SQLException {
		return userRepository.insertUser(user);
	}
	
	public UserDTO updateUser(User user, Integer id)throws SQLException {
		return userRepository.updateUser(user, id);
	}
	
	public boolean deleteUser(Integer userId)throws SQLException {
		return userRepository.deleteOne(userId);
	}
	
	public boolean isSessionUserAuthorizedorAdmin(HttpSession session, Integer userId) {
		
		Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
		if (session.getAttribute("access").equals("admin") || sessionId == userId) {
			return true;
		}
		return false;
	}
}
