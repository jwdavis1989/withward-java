package com.withward.repository;

import java.sql.Connection;
//import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.withward.model.Withlist;
import com.withward.util.JDBC;

public class WithlistDAO {
	/**
	 * Method to interact with the database to get all withlist records that belong
	 * to a specific user and return them as an ArrayList.
	 * 
	 * @param user_id id value of user
	 * @return ArrayList<Withlist>
	 * @throws SQLException
	 */
	public ArrayList<Withlist> getAll(Integer user_id) throws SQLException {

		ArrayList<Withlist> withlists = new ArrayList<Withlist>();
		String sql = "SELECT * " + "FROM withlists " + "WHERE owner_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, user_id);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("withlist_id");
			Integer ownerId = rs.getInt("owner_id");
			String title = rs.getString("withlist_title");
			String description = rs.getString("withlist_description");
			Withlist withlist = new Withlist(id, ownerId, title, description);
			withlists.add(withlist);
		}
		pstmt.close();
		connection.close();
		return withlists;
	}

	/**
	 * Method to interact with the database to confirm if the requesting user is the
	 * owner of the withlist
	 * 
	 * @param user_id     id value of user
	 * @param withlist_id id value of withlist
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isAdmin(Integer user_id, Integer withlist_id) throws SQLException {
		String sql = "SELECT owner_id " + "FROM withlists " + "WHERE withlist_id = ?";

		Connection connection = JDBC.getConnection();

		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, withlist_id);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Integer ownerId = rs.getInt("owner_id");
			if (ownerId == user_id) {
				return true;
			}
		}
		pstmt.close();
		connection.close();

		return false;
	}

	/**
	 * Method to interact with the database to get one withlist record.
	 * 
	 * @param withlist_id id value of withlist
	 * @return Withlist object of found withlist record.
	 * @throws SQLException
	 */
	public Withlist getWithlist(Integer withlist_id) throws SQLException {

		Withlist withlist = null;
		String sql = "SELECT * " + "FROM withlists " + "WHERE withlist_id = ?";
		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, withlist_id);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("withlist_id");
			Integer ownerId = rs.getInt("owner_id");
			String title = rs.getString("withlist_title");
			String description = rs.getString("withlist_description");
			withlist = new Withlist(id, ownerId, title, description);
		}
		pstmt.close();
		connection.close();

		return withlist;
	}

	/**
	 * Method to interact with the database to insert one withlist record.
	 * 
	 * @param Withlist object
	 * @return Withlist object of inserted withlist record.
	 * @throws SQLException
	 */
	public Withlist insertWithlist(Withlist withlist) throws SQLException {
		String sql = "INSERT INTO withlists " + "(owner_id, withlist_title, withlist_description) " + "VALUES "
				+ "(?,?,?)";

		Connection connection = JDBC.getConnection();

		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, withlist.getOwnerId());
		pstmt.setString(2, withlist.getTitle());
		pstmt.setString(3, withlist.getDescription());

		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("No Rows Affected");
		}
		
		Withlist newWithlist = null;

		int autoId = 0;
		ResultSet generatedKeys = pstmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			autoId = generatedKeys.getInt(1);
			newWithlist = new Withlist(autoId, withlist.getOwnerId(), withlist.getTitle(), withlist.getDescription());
		} else {
			throw new SQLException("ID generation failed");
		}

		pstmt.close();
		connection.close();
		return newWithlist;
	}

	/**
	 * Method to interact with the database to update one withlist record.
	 * 
	 * @param Withlist object
	 * @param id       withlist id that will be updated
	 * @return Withlist object of updated withlist record.
	 * @throws SQLException
	 */
	public Withlist updateWithlist(Withlist withlist, Integer id) throws SQLException {
		String sql = "UPDATE withlists " + "SET withlist_title = ?, " + "withlist_description = ? "
				+ "WHERE withlist_id = ?";

		Connection connection = JDBC.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement pstmt = connection.prepareStatement(sql);

		pstmt.setString(1, withlist.getTitle());
		pstmt.setString(2, withlist.getDescription());
		pstmt.setInt(4, id);

		Withlist updatedWithlist = null;
		
		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("No Rows Affected");
		} else {
			updatedWithlist = new Withlist(id, withlist.getOwnerId(), withlist.getTitle(), withlist.getDescription());
		}

		pstmt.close();
		connection.close();
		return updatedWithlist;
	}

	/**
	 * Method to interact with the database to delete one withlist record.
	 * 
	 * @param withlist_id withlist id that will be deleted
	 * @return boolean if record was deleted
	 * @throws SQLException
	 */
	public boolean deleteOne(Integer withlist_id) throws SQLException {

		String sql = "DELETE FROM withlists WHERE withlist_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, withlist_id);
		if (pstmt.executeUpdate() == 1) {
			pstmt.close();
			connection.close();
			return true;
		} else {
			pstmt.close();
			connection.close();
			throw new SQLException("No Rows Affected");
		}
	}
}
