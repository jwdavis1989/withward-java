package com.withward.service;
import java.sql.SQLException;
import java.util.ArrayList;

import com.withward.model.DestinationRating;
import com.withward.repository.DestinationRatingDAO;

public class DestinationRatingService {
	private DestinationRatingDAO destRatingRepo = new DestinationRatingDAO();
	
	public ArrayList<DestinationRating> getAllDestinationRatings(Integer destination_id) throws SQLException{
		return destRatingRepo.getAll(destination_id);
	}
	
	public DestinationRating getOneRating(Integer destinationRating_id) throws SQLException{
		return destRatingRepo.getDestinationRating(destinationRating_id);
	}
	
	public void createDestinationRating(DestinationRating destination_rating)throws SQLException {
		destRatingRepo.insertDestinationRating(destination_rating);
	}
	
	public DestinationRating updateDestinationRating(DestinationRating destination_rating, Integer id)throws SQLException {
		return destRatingRepo.updateDestinationRating(destination_rating, id);
	}

}
