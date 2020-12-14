package com.withward.servlets;

import java.util.ArrayList;
import java.io.BufferedReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.withward.service.UserService;
import com.withward.DTO.UserDTO;
import com.withward.model.User;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(UserServlet.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	private UserService userService = new UserService();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) IF a USER is logged in THEN they SHOULD be able to see all
	 *      users IF a USER is not logged in THEN they SHOULD get a 401
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		logger.info("GET request made to " + req.getRequestURI());

		if (session != null) {
			if (req.getPathInfo() != null && req.getPathInfo().split("/").length == 2) {
				try {
					Integer id = Integer.parseInt(req.getPathInfo().split("/")[1]);
					UserDTO user = userService.getOneUser(id);
					
					if (user != null) {						
						objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
						String json = objectMapper.writeValueAsString(user);
						
						res.getWriter().append(json);
						res.setContentType("application/json");
						res.setStatus(200);
					} else {
						res.setStatus(400);
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
					ArrayList<UserDTO> users = userService.getAllUsers();

					objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
					String json = objectMapper.writeValueAsString(users);

					res.getWriter().append(json);
					res.setContentType("application/json");
					res.setStatus(200);

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
	 * 
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		logger.info("POST request made to " + req.getRequestURI());

		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		String jsonString = sb.toString();

		try {
			User userdata = objectMapper.readValue(jsonString, User.class);
			if (userdata.getUsername() != null && userdata.getEmail() != null && userdata.getPassword() != null) {
				if (userService.getByUsername(userdata.getUsername()) == null) {
					UserDTO user = userService.createUser(userdata);
					if (user != null) {						
						String insertedUserJSON = objectMapper.writeValueAsString(user);
						
						res.getWriter().append(insertedUserJSON);
						res.setContentType("application/json");
						res.setStatus(201);
					} else {
						res.setStatus(400);
					}
				} else {
					logger.debug("Login Creation Failed, Username is already in use.");
					res.setStatus(400);
				}
			} else {
				logger.debug("Login Creation Failed, username, email, password must not be null.");
				res.setStatus(400);
			}

		} catch (JsonProcessingException e) {
			res.setStatus(400);
			e.printStackTrace();
		} catch (SQLException e) {
			res.setStatus(400);
			e.printStackTrace();
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

					User userdata = objectMapper.readValue(jsonString, User.class);

					Integer paramId = Integer.parseInt(params[1]);
					if (userService.isSessionUserAuthorizedorAdmin(session, paramId)) {
						UserDTO user = userService.updateUser(userdata, paramId);
						if (user != null) {
							String insertedUserJSON = objectMapper.writeValueAsString(user);

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
					if (userService.isSessionUserAuthorizedorAdmin(session, paramId)) {
						if (userService.deleteUser(paramId)) {
							logger.info("DELETE authorized, user account deleted and session terminated.");
							session.invalidate();
							res.setStatus(204);
						} else {
							res.setStatus(400);
						}

					} else {
						logger.info("DELETE user attempt without authorization");
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
