package com.withward.servlets;

import java.io.BufferedReader;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.withward.DTO.DestinationDTO;
import com.withward.model.DestinationRating;
import com.withward.service.DestinationService;
import com.withward.service.RatingService;
import com.withward.service.UserService;
import com.withward.service.WithlistService;

/**
 * Servlet implementation class RatingServlet
 */
public class RatingServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(DestinationServlet.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	private RatingService ratingService = new RatingService();
	private DestinationService destinationService = new DestinationService();
	private WithlistService withlistService = new WithlistService();
	private UserService userService = new UserService();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RatingServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("GET request made to " + req.getRequestURI());
		HttpSession session = req.getSession(false);

		if (session != null) {

			if (req.getPathInfo() != null && req.getPathInfo().split("/").length == 2) {
				try {
					Integer id = Integer.parseInt(req.getPathInfo().split("/")[1]);
					DestinationRating rating = ratingService.getOneDestinationRating(id);
					if (rating != null) {
						Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
						if (session.getAttribute("access").equals("admin") || sessionId == rating.getUser_id()) {
							objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
							String json = objectMapper.writeValueAsString(rating);

							res.getWriter().append(json);
							res.setContentType("application/json");
							res.setStatus(200);
						} else {
							res.setStatus(401);
						}
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
			} else {
				res.setStatus(400);
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
				DestinationRating ratingData = objectMapper.readValue(jsonString, DestinationRating.class);
				if (ratingData.getRating() != null && ratingData.getUser_id() != null
						&& ratingData.getDestination_id() != null) {

					Integer destinationId = ratingData.getDestination_id();
					DestinationDTO destination = destinationService.getOneDestination(destinationId);

					if (destination != null) {

						Integer withlistId = destination.getWithlist_id();
						Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
						if (session.getAttribute("access").equals("admin")
								|| withlistService.isMember(sessionId, withlistId)) {
							DestinationRating rating = ratingService.createDestinationRating(ratingData);
							if (rating != null) {
								String insertedUserJSON = objectMapper.writeValueAsString(rating);

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
				} else {
					res.setStatus(400);
					logger.debug("Null values in rating, user id, or destination id.");
				}

			} catch (JsonProcessingException e) {
				res.setStatus(400);
				e.printStackTrace();
			} catch (SQLException e) {
				res.setStatus(400);
				e.printStackTrace();
			}
		} else {
			logger.info("GET request made without login");
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
					DestinationRating ratingData = objectMapper.readValue(jsonString, DestinationRating.class);

					if (ratingData.getId() != null && ratingData.getRating() != null
							&& ratingData.getUser_id() != null) {

						if (userService.isSessionUserAuthorizedorAdmin(session, ratingData.getUser_id())) {
							DestinationRating rating = ratingService.updateDestinationRating(ratingData,
									Integer.parseInt(params[1]));
							String insertedUserJSON = objectMapper.writeValueAsString(rating);

							res.getWriter().append(insertedUserJSON);
							res.setContentType("application/json");
							res.setStatus(201);
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
			}
		} else {
			logger.info("GET request made without login");
			res.setStatus(401);
		}
	}
}
