package com.withward.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.Statement;
import java.util.ArrayList;

import com.withward.model.Destination;
import com.withward.util.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DestinationDAO {
	
	/**
	 * Method to interact with the database to get all destination records
	 * that belong to a specific withlist and return them as an ArrayList.
	 * @param withlist_id id value of withlist
	 * @return ArrayList<Destination> of all destination records. 
	 * @throws SQLException
	 */
	public ArrayList<Destination> getAll(Integer withlist_id) throws SQLException {

		ArrayList<Destination> destinations = new ArrayList<Destination>();
		String sql = "SELECT * " + "FROM destinations " + "WHERE withlist_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, withlist_id);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			Integer id = rs.getInt("dest_id");
			Integer withlistId = rs.getInt("withlist_id");
			String name = rs.getString("dest_name");
			String description = rs.getString("dest_description");
			String photo = rs.getString("dest_photo");
			boolean completed = rs.getBoolean("dest_completed");
			Float averageRating = rs.getFloat("dest_averageRating");
			Destination destination = new Destination(id, withlistId, name, description, photo, completed,
					averageRating);
			destinations.add(destination);
		}
		
		pstmt.close();
		connection.close();
		return destinations;
	}

	/**
	 * Method to interact with the database to get one destination record.
	 * @param destinationId id value of destination
	 * @return Destination object of found destination record. 
	 * @throws SQLException
	 */
	public Destination getDestination(Integer destinationId) throws SQLException {

		Destination destination = null;
		String sql = "SELECT * " + "FROM destinations " + "WHERE dest_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, destinationId);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			Integer id = rs.getInt("dest_id");
			Integer withlistId = rs.getInt("withlist_id");
			String name = rs.getString("dest_name");
			String description = rs.getString("dest_description");
			String photo = rs.getString("dest_photo");
			boolean completed = rs.getBoolean("dest_completed");
			Float averageRating = rs.getFloat("dest_averageRating");
			destination = new Destination(id, withlistId, name, description, photo, completed, averageRating);
		}
		pstmt.close();
		connection.close();

		return destination;
	}

	/**
	 * Method to interact with the database to insert one destination record.
	 * @param Destination object
	 * @return Destination object of inserted destination record. 
	 * @throws SQLException
	 */
	public Destination insertDestination(Destination destination) throws SQLException {
		String sql = "INSERT INTO destinations "
				+ "(withlist_id, dest_name, dest_description, dest_photo, dest_completed, dest_averageRating) "
				+ "VALUES " + "(?,?,?,?,?,?)";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, destination.getWithlist_id());
		pstmt.setString(2, destination.getName());
		pstmt.setString(3, destination.getDescription());
		pstmt.setString(4, destination.getPhoto());
		pstmt.setBoolean(5, false);
		pstmt.setFloat(6, destination.getAverageRating());

		Destination newDestination = null;
		
		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("No Rows Affected");
		}

		int autoId = 0;
		ResultSet generatedKeys = pstmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			autoId = generatedKeys.getInt(1);
			newDestination = new Destination(autoId, destination.getWithlist_id(), destination.getName(),
					destination.getDescription(), destination.getPhoto(), destination.isCompleted(),
					destination.getAverageRating());
		} else {
			throw new SQLException("ID generation failed");
		}
		pstmt.close();
		connection.close();
		return newDestination;

	}

	/**
	 * Method to interact with the database to update one destination record.
	 * @param Destination object
	 * @param id destination id that will be updated
	 * @return Destination object of updated destination record. 
	 * @throws SQLException
	 */
	public Destination updateDestination(Destination destination, Integer id) throws SQLException {
		String sql = "UPDATE destinations " + "SET dest_name = ?, " + "dest_description = ?, " + "dest_photo = ?, "
				+ "dest_completed = ?, " + "dest_averageRating = ? " + "WHERE dest_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);

		pstmt.setString(1, destination.getName());
		pstmt.setString(2, destination.getDescription());
		pstmt.setString(3, destination.getPhoto());
		pstmt.setBoolean(4, destination.isCompleted());
		pstmt.setFloat(5, destination.getAverageRating());
		pstmt.setInt(6, id);

		Destination updateDestination = null;

		if (pstmt.executeUpdate() != 1) {
			throw new SQLException("No Rows Affected");
		} else {
			updateDestination = new Destination(id, destination.getWithlist_id(), destination.getName(), destination.getDescription(),
					destination.getPhoto(), destination.isCompleted(), destination.getAverageRating());
		}

		pstmt.close();
		connection.close();

		return updateDestination;
	}

	/**
	 * Method to interact with the database to delete one destination record.
	 * @param id destination id that will be deleted
	 * @return boolean if record was deleted 
	 * @throws SQLException
	 */
	public boolean deleteOne(Integer destination_id) throws SQLException {

		String sql = "DELETE FROM destinations WHERE dest_id = ?";

		Connection connection = JDBC.getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, destination_id);
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
