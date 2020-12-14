package com.withward.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.withward.model.DestinationRating;
import com.withward.repository.DestinationRatingDAO;

public class RatingService {

	private DestinationRatingDAO ratingRepository = new DestinationRatingDAO();

	public ArrayList<DestinationRating> getAllRatings(Integer destinationId) throws SQLException {
		return ratingRepository.getAll(destinationId);
	}

	public DestinationRating getOneDestinationRating(Integer destinationId) throws SQLException {
		return ratingRepository.getDestinationRating(destinationId);
	}

	public DestinationRating createDestinationRating(DestinationRating destination_rating) throws SQLException {
		return ratingRepository.insertDestinationRating(destination_rating);
	}

	public DestinationRating updateDestinationRating(DestinationRating rating, Integer id) throws SQLException {
		return ratingRepository.updateDestinationRating(rating, id);
	}

}
