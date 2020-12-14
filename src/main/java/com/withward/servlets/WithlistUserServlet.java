package com.withward.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withward.model.WithlistUser;
import com.withward.service.WithlistService;

/**
 * Servlet implementation class WithlistUser
 */
public class WithlistUserServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(WithlistServlet.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private WithlistService withlistService = new WithlistService();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WithlistUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("POST request made to " + req.getRequestURI());

		HttpSession session = req.getSession(false);
		
		if (session != null) {
			try {
				
				Integer userId = Integer.parseInt(req.getParameter("user-id"));
				Integer withlistId = Integer.parseInt(req.getParameter("withlist-id"));
				if (userId != null && withlistId != null) {					
					Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
					if (withlistService.isAdmin(sessionId, withlistId)) {
						
						WithlistUser withlistuser = withlistService.addUserToWithlist(userId, withlistId);
						if (withlistuser != null) {							
							String insertedUserJSON = objectMapper.writeValueAsString(withlistuser);
							
							res.getWriter().append(insertedUserJSON);
							res.setContentType("application/json");
							res.setStatus(201);
						} else {
							res.setStatus(400);
						}
					} else {
						res.setStatus(401);
					}
				} else {
					res.setStatus(400);
				}
	

			} catch (JsonProcessingException e) {
				res.setStatus(400);
				e.printStackTrace();
			} catch (SQLException e) {
				res.setStatus(400);
				e.printStackTrace();
			}
		} else {
			logger.info("POST request made without login");
			res.setStatus(401);
		}

	}

}
