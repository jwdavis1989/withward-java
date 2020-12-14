package com.withward.servlets;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.withward.DTO.WithlistDTO;
import com.withward.model.Withlist;
import com.withward.model.WithlistUser;
import com.withward.service.UserService;
import com.withward.service.WithlistService;

/**
 * Servlet implementation class WithlistServlet
 */
public class WithlistServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(WithlistServlet.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private WithlistService withlistService = new WithlistService();
	private UserService userService = new UserService();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WithlistServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *      
	 *      
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("GET request made to " + req.getRequestURI());

		HttpSession session = req.getSession(false);

		if (session != null) {
			if (req.getPathInfo() != null && req.getPathInfo().split("/").length == 2) {
				try {
					Integer withlistId = Integer.parseInt(req.getPathInfo().split("/")[1]);
					Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());

					if (withlistService.isMember(sessionId, withlistId)) {							
						WithlistDTO withlist = withlistService.getOneWithlist(withlistId);
						if (withlist != null) {														
							objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
							String json = objectMapper.writeValueAsString(withlist);
							
							res.getWriter().append(json);
							res.setContentType("application/json");
							res.setStatus(200);
						} else {
							res.setStatus(400);
						}
					} else {
						res.setStatus(401);
					}
				} catch (NumberFormatException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (SQLException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					res.setStatus(400);
					e.printStackTrace();
				}
			} else if (req.getPathInfo() != null && req.getPathInfo().split("/").length > 2) {
				res.setStatus(400);
			} else {
				try {
					if (req.getParameter("user-id") != null) {
						Integer userId = Integer.parseInt(req.getParameter("user-id"));
						
						if (userService.isSessionUserAuthorizedorAdmin(session, userId)) {							
							ArrayList<Withlist> withlists = withlistService.getAllWithlists(userId);
							
							objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
							String json = objectMapper.writeValueAsString(withlists);
							
							res.getWriter().append(json);
							res.setContentType("application/json");
							res.setStatus(200);
						} else {
							res.setStatus(401);
						}
						
					} else {
						res.setStatus(404);
					}

				} catch (JsonProcessingException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (IOException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (SQLException e) {
					res.setStatus(400);
					e.printStackTrace();
				}
			}
		} else {
			logger.info("GET request made without login");
			res.setStatus(401);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("POST request made to " + req.getRequestURI());

		HttpSession session = req.getSession(false);

		if (session != null) {
			BufferedReader reader = req.getReader();
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			String jsonString = sb.toString();

			try {
				Withlist withlistData = objectMapper.readValue(jsonString, Withlist.class);

				if (withlistData.getDescription() != null && withlistData.getTitle() != null
						&& withlistData.getOwnerId() != null) {
					if (userService.isSessionUserAuthorizedorAdmin(session, withlistData.getOwnerId())) {						
						Withlist withlist = withlistService.createWithlist(withlistData);
						WithlistUser wlUser = withlistService.addUserToWithlist(withlist.getOwnerId(), withlist.getId());
						if (withlist != null && wlUser != null) {
							
								String insertedUserJSON = objectMapper.writeValueAsString(withlist);
								res.getWriter().append(insertedUserJSON);
								res.setContentType("application/json");
								res.setStatus(201);

						} else {
							res.setStatus(400);
						}
					} else {
						logger.debug("Withlist creation failed. Unauthorized request");
						res.setStatus(401);
					}
					
				} else {
					logger.debug("Withlist creation failed. Withlist data cannot be null.");
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

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("PUT request made to " + req.getRequestURI());

		HttpSession session = req.getSession(false);

		if (session != null) {
			if (req.getPathInfo() == null || req.getPathInfo().split("/").length != 2) {
				res.setStatus(400);
			} else {

				try {
					String[] params = req.getPathInfo().split("/");
					BufferedReader reader = req.getReader();
					StringBuilder sb = new StringBuilder();
					String line;

					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}

					String jsonString = sb.toString();
					
					Integer paramId = Integer.parseInt(params[1]);
					Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
					if (session.getAttribute("access").equals("admin") || withlistService.isAdmin(sessionId, paramId)) {						
						Withlist withlistData = objectMapper.readValue(jsonString, Withlist.class);
						Withlist withlist = withlistService.updateWithlist(withlistData, paramId);
						if (withlist != null) {
							String insertedUserJSON = objectMapper.writeValueAsString(withlist);
							
							res.getWriter().append(insertedUserJSON);
							res.setContentType("application/json");
							res.setStatus(201);
						} else {
							res.setStatus(400);
						}
					} else {
						res.setStatus(401);
					}
					
				} catch (JsonProcessingException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (SQLException e) {
					res.setStatus(400);
					e.printStackTrace();
				}
			}
		} else {
			logger.info("PUT request made without login");
			res.setStatus(401);
		}

	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("DELETE request made to " + req.getRequestURI());

		HttpSession session = req.getSession(false);

		if (session != null) {
			if (req.getPathInfo() == null || req.getPathInfo().split("/").length != 2) {
				res.setStatus(400);
			} else {
				try {
					String[] params = req.getPathInfo().split("/");
					Integer paramId = Integer.parseInt(params[1]);
					Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
					if (session.getAttribute("access").equals("admin") || withlistService.isAdmin(sessionId, paramId)) {
						withlistService.deleteWithlist(Integer.parseInt(params[1]));
						res.setStatus(204);
					} else {
						res.setStatus(401);
					}
				} catch (NumberFormatException e) {
					res.setStatus(400);
					e.printStackTrace();
				} catch (SQLException e) {
					res.setStatus(400);
					e.printStackTrace();
				}
			}
		} else {
			logger.info("DELETE request made without login");
			res.setStatus(401);
		}

	}
}
