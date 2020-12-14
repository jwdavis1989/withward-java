package com.withward.service;
import java.sql.SQLException;
import java.util.ArrayList;

import com.withward.DTO.UserDTO;
import com.withward.DTO.WithlistDTO;
import com.withward.model.Destination;
import com.withward.model.Withlist;
import com.withward.model.WithlistUser;
import com.withward.repository.DestinationDAO;
import com.withward.repository.WithlistDAO;
import com.withward.repository.WithlistUserDAO;

public class WithlistService {
	WithlistDAO wlRepo = new WithlistDAO();
	DestinationDAO destRepository = new DestinationDAO();
	WithlistUserDAO wlUserRepo = new WithlistUserDAO();
	
	public WithlistService() {
		super();
	}
	
	public WithlistService(WithlistDAO wlRepo, DestinationDAO dRepo) {
		this.wlRepo = wlRepo;
		this.destRepository = dRepo;
	}
	
	public ArrayList<Withlist> getAllWithlists(Integer userId) throws SQLException {
		return wlRepo.getAll(userId);
	}
	
	public WithlistDTO getOneWithlist(Integer withlistId) throws SQLException {
		Withlist withlist = wlRepo.getWithlist(withlistId);
		if (withlist != null) {			
			ArrayList<Destination> destinations = destRepository.getAll(withlistId);
			ArrayList<UserDTO> users = wlUserRepo.getAllWithlistUsers(withlistId);
			return new WithlistDTO(withlist.getId(), withlist.getOwnerId(), withlist.getTitle(), withlist.getDescription(), destinations, users);
		} else {
			return null;
		}
	}
	
	public boolean isAdmin(Integer user_id, Integer withlist_id) throws SQLException {
		return wlRepo.isAdmin(user_id, withlist_id);
	}
	
	public boolean isMember(Integer user_id, Integer withlist_id) throws SQLException {
		ArrayList<UserDTO> users = wlUserRepo.getAllWithlistUsers(withlist_id);
		for (UserDTO user : users) {
			if (user.getId() == user_id) {
				return true;
			}
		}
		return false;
	}
	
	public WithlistUser addUserToWithlist(Integer userId, Integer withlistId) throws SQLException {
		return wlUserRepo.createWithlistUser(userId, withlistId);
	}
	
	public Withlist createWithlist(Withlist withlist) throws SQLException {
		return wlRepo.insertWithlist(withlist);
	}
	
	public Withlist updateWithlist(Withlist withlist, Integer id)  throws SQLException{
		return wlRepo.updateWithlist(withlist, id);
	}
	
	public boolean deleteWithlist(Integer withlistId) throws SQLException {
		return wlRepo.deleteOne(withlistId);
	}
	
}
