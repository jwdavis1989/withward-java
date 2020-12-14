package com.withward.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.withward.DTO.DestinationDTO;
import com.withward.model.Destination;
import com.withward.model.DestinationRating;
import com.withward.repository.DestinationDAO;
import com.withward.repository.DestinationRatingDAO;

public class DestinationService {

	private DestinationDAO destRepository = new DestinationDAO();
	private DestinationRatingDAO ratingRepository = new DestinationRatingDAO();

	public ArrayList<Destination> getAllDestinations(Integer withlistId) throws SQLException {
		return destRepository.getAll(withlistId);
	}

	public DestinationDTO getOneDestination(Integer destinationId) throws SQLException {

		Destination destination = destRepository.getDestination(destinationId);
		if (destination != null) {			
			ArrayList<DestinationRating> ratings = ratingRepository.getAll(destinationId);
			return new DestinationDTO(destinationId, destination.getWithlist_id(), destination.getName(),
					destination.getDescription(), destination.getPhoto(), destination.isCompleted(),
					destination.getAverageRating(), ratings);
		} else {
			return null;
		}
	}
	
//	public boolean Destination

	public Destination createDestination(Destination destination) throws SQLException {
		return destRepository.insertDestination(destination);
	}

	public Destination updateDestination(Destination destination, Integer id) throws SQLException {
		return destRepository.updateDestination(destination, id);
	}

	public boolean deleteDestination(Integer destinationId) throws SQLException {
		return destRepository.deleteOne(destinationId);
	}
}
