package com.withward.repository;

import java.sql.Connection;
//import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.withward.model.DestinationRating;
import com.withward.util.JDBC;

public class DestinationRatingDAO {

	/**
	 * Method to interact with the database to get all destination records that
	 * belong to a specific destination and return them as an ArrayList.
	 * 
	 * @param destination_id id value of destination
	 * @return ArrayList<DestinationRating>
	 * @throws SQLException
	 */
	public ArrayList<DestinationRating> getAll(Integer destination_id) throws SQLException {

		ArrayList<DestinationRating> destination_ratings = new ArrayList<DestinationRating>();
		String sql = "SELECT * " + "FROM ratings" + " WHERE destination_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, destination_id);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("rating_id");
			Integer destinationId = rs.getInt("destination_id");
			Integer userId = rs.getInt("user_id");
			Float rating = rs.getFloat("rating_value");
			DestinationRating destination_user = new DestinationRating(id, destinationId, userId, rating);
			destination_ratings.add(destination_user);
		}
		pstmt.close();
		connection.close();
		return destination_ratings;
	}

	/**
	 * Method to interact with the database to get one rating record.
	 * 
	 * @param destinationRatingId id value of rating
	 * @return DestinationRating object of found rating record.
	 * @throws SQLException
	 */
	public DestinationRating getDestinationRating(Integer destinationRatingId) throws SQLException {

		DestinationRating destinationRating = null;
		String sql = "SELECT * " + "FROM ratings " + "WHERE rating_id = ?";
		Connection connection = JDBC.getConnection();

		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, destinationRatingId);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("rating_id");
			Integer destinationId = rs.getInt("destination_id");
			Integer userId = rs.getInt("user_id");
			Float rating = rs.getFloat("rating_value");
			destinationRating = new DestinationRating(id, destinationId, userId, rating);
		}
		pstmt.close();
		connection.close();
		return destinationRating;
	}

	/**
	 * Method to interact with the database to insert one rating record.
	 * 
	 * @param DestinationRating object
	 * @return DestinationRating object of inserted rating record.
	 * @throws SQLException
	 */
	public DestinationRating insertDestinationRating(DestinationRating destination_rating) throws SQLException {
		String sql = "INSERT INTO ratings " + "(destination_id, user_id, rating_value) " + "VALUES " + "(?,?,?)";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, destination_rating.getDestination_id());
		pstmt.setInt(2, destination_rating.getUser_id());
		pstmt.setFloat(3, destination_rating.getRating());

		DestinationRating newRating = null;
		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("Inserting user failed, no rows were affected");
		}

		int autoId = 0;
		ResultSet generatedKeys = pstmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			autoId = generatedKeys.getInt(1);
			newRating =  new DestinationRating(autoId, destination_rating.getDestination_id(), destination_rating.getUser_id(),
					destination_rating.getRating());
		} else {
			throw new SQLException("Inserting user failed, no ID generated.");
		}

		pstmt.close();
		connection.close();
		return newRating;
	}

	/**
	 * Method to interact with the database to update one rating record.
	 * 
	 * @param DestinationRating object
	 * @param id                rating id that will be updated
	 * @return DestinationRating object of updated rating record.
	 * @throws SQLException
	 */
	public DestinationRating updateDestinationRating(DestinationRating destination_rating, Integer id)
			throws SQLException {
		String sql = "UPDATE ratings " + "SET rating_value = ? " + "WHERE rating_id = ?";

		Connection connection = JDBC.getConnection();

		PreparedStatement pstmt = connection.prepareStatement(sql);

		pstmt.setFloat(1, destination_rating.getRating());
		pstmt.setInt(2, id);
		
		DestinationRating newRating = null;
		
		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("Inserting destination failed, no rows were affected");
		} else {
			newRating = new DestinationRating(id, destination_rating.getDestination_id(), destination_rating.getUser_id(),
					destination_rating.getRating());
		}

		pstmt.close();
		connection.close();
		return newRating;
	}
}
