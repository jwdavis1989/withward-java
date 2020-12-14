package com.withward.repository;

import java.sql.Connection;
//import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
import java.util.ArrayList;

import com.withward.DTO.UserDTO;
import com.withward.model.WithlistUser;
import com.withward.util.JDBC;

public class WithlistUserDAO {
	/**
	 * Method to interact with the database to get all withlist-user records that
	 * belong to a specific withlist and return them as an ArrayList.
	 * 
	 * @param withlist_id id value of withlist
	 * @return ArrayList<UserDTO>
	 * @throws SQLException
	 */
	public ArrayList<UserDTO> getAllWithlistUsers(Integer withlist_id) throws SQLException {

		ArrayList<UserDTO> withlist_users = new ArrayList<UserDTO>();
		String sql = "SELECT * " + "FROM withlistusers " + "INNER JOIN users "
				+ "ON withlistusers.user_id = users.user_id" + " WHERE withlistusers.withlist_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, withlist_id);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("user_id");
			String username = rs.getString("username");
			String photo = rs.getString("user_photo");
			UserDTO user = new UserDTO(id, username, photo);
			withlist_users.add(user);
		}
		pstmt.close();
		connection.close();
		return withlist_users;
	}

	/**
	 * Method to interact with the database to insert one withlistUser record.
	 * 
	 * @param userId     id value of user
	 * @param withlistId id value of withlist
	 * @return WithlistUser
	 * @throws SQLException
	 */
	public WithlistUser createWithlistUser(Integer userId, Integer withlistId) throws SQLException {
		String sql = "INSERT INTO withlistusers " + "(withlist_id, user_id) " + "VALUES " + "(?,?)";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, withlistId);
		pstmt.setInt(2, userId);

		WithlistUser newWLUser = null;
		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("Inserting user failed, no rows were affected");
		}

		int autoId = 0;
		ResultSet generatedKeys = pstmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			autoId = generatedKeys.getInt(1);
			newWLUser = new WithlistUser(autoId, withlistId, userId);

		} else {
			throw new SQLException("Inserting user failed, no ID generated.");
		}
		pstmt.close();
		connection.close();
		return newWLUser;
	}
}
