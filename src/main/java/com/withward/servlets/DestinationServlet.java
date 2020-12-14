package com.withward.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
import com.withward.model.Destination;
import com.withward.service.DestinationService;
import com.withward.service.WithlistService;

/**
 * Servlet implementation class DestinationServlet
 */
public class DestinationServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(DestinationServlet.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	private DestinationService destinationService = new DestinationService();
	private WithlistService withlistService = new WithlistService();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DestinationServlet() {
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
					if (req.getParameter("withlist-id") != null) {						
						Integer id = Integer.parseInt(req.getPathInfo().split("/")[1]);
						Integer withlistId = Integer.parseInt(req.getParameter("withlist-id"));
						Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());

						if (withlistService.isMember(sessionId, withlistId)) {							
							DestinationDTO destination = destinationService.getOneDestination(id);
							
							if (destination != null) {
								objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
								String json = objectMapper.writeValueAsString(destination);
								
								res.getWriter().append(json);
								res.setContentType("application/json");
								res.setStatus(200);
							} else {
								res.setStatus(400);
							}
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
			} else if (req.getPathInfo() != null && req.getPathInfo().split("/").length > 2) {
				res.setStatus(400);
			} else {
				try {
					if (req.getParameter("withlist-id") != null) {
						Integer withlistId = Integer.parseInt(req.getParameter("withlist-id"));
						Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());

						if (withlistService.isMember(sessionId, withlistId)) {							
							ArrayList<Destination> users = destinationService.getAllDestinations(withlistId);
							
							objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
							String json = objectMapper.writeValueAsString(users);
							
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

			try {
				BufferedReader reader = req.getReader();
				StringBuilder sb = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				String jsonString = sb.toString();
				Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
				Destination destinationData = objectMapper.readValue(jsonString, Destination.class);
				if (destinationData.getDescription() != null && destinationData.getName() != null
						&& destinationData.getWithlist_id() != null) {

					if (session.getAttribute("access").equals("admin")
							|| withlistService.isMember(sessionId, destinationData.getWithlist_id())) {

						Destination destination = destinationService.createDestination(destinationData);
						if (destination != null) {
							String insertedUserJSON = objectMapper.writeValueAsString(destination);

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
					logger.debug("POST request made but destination description, name, and withlist id were null");
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

					Destination destinationData = objectMapper.readValue(jsonString, Destination.class);
					Integer destinationId = Integer.parseInt(params[1]);
					Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
					DestinationDTO destExists = destinationService.getOneDestination(destinationId);
					if (destExists != null) {
						if (session.getAttribute("access").equals("admin")
								|| withlistService.isMember(sessionId, destExists.getWithlist_id())) {
							Destination destination = destinationService.updateDestination(destinationData,
									Integer.parseInt(params[1]));
							if (destination != null) {
								String insertedUserJSON = objectMapper.writeValueAsString(destination);

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
			logger.info("GET request made without login");
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
					Integer destinationId = Integer.parseInt(params[1]);
					Integer sessionId = Integer.parseInt(session.getAttribute("userId").toString());
					DestinationDTO destination = destinationService.getOneDestination(destinationId);
					if (destination != null) {
						if (session.getAttribute("access").equals("admin")
								|| withlistService.isMember(sessionId, destination.getWithlist_id())) {
							if (destinationService.deleteDestination(destinationId)) {
								logger.info("DELETE authorized, destination deleted");
								res.setStatus(204);
							} else {
								res.setStatus(400);
							}
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
				}
			}
		} else {
			logger.info("GET request made without login");
			res.setStatus(401);
		}
	}
}
