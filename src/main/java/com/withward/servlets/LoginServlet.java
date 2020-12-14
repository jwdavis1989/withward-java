package com.withward.servlets;

import java.io.BufferedReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(WithlistServlet.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private UserService userService = new UserService();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) Logout method to logout user
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		if (session != null) {
			logger.info((String) session.getAttribute("username") + " logged out.");
			session.invalidate();
		} else {
			logger.info("Log out attempt while not logged in.");
			res.setStatus(401);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		String jsonString = sb.toString();
		HttpSession session = req.getSession();

		if (req.getPathInfo() != null && req.getPathInfo().split("/").length == 2) {
			try {
				String authLogin = req.getPathInfo().split("/")[1];
				logger.debug("authLogin");
				if (authLogin.equals("admin")) {
					User loginData = objectMapper.readValue(jsonString, User.class);
					String username = loginData.getUsername();
					String password = loginData.getPassword();
					if (username != null && password != null) {
						username = loginData.getUsername();
						password = loginData.getPassword();
					} else {
						username = (String) session.getAttribute("username");
						password = (String) session.getAttribute("password");
					}

					if (userService.isAuthenticated(username, password) && userService.isAdmin(username)) {
						session.setAttribute("username", username);
						session.setAttribute("password", password);
						session.setAttribute("access", "admin");

						UserDTO userData = userService.getByUsername(username);
						session.setAttribute("userId", userData.getId());
						res.getWriter().append("YOU LOGGED IN as an ADMIN");
						res.setStatus(200);
					} else {
						res.getWriter().append("INCORRECT LOGIN");
						res.setStatus(401);
					}
				}
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
			try {
				User loginData = objectMapper.readValue(jsonString, User.class);
				String username = loginData.getUsername();
				String password = loginData.getPassword();
				if (username != null && password != null) {
					username = loginData.getUsername();
					password = loginData.getPassword();
				} else {
					username = (String) session.getAttribute("username");
					password = (String) session.getAttribute("password");
				}

				if (userService.isAuthenticated(username, password)) {
					session.setAttribute("username", username);
					session.setAttribute("password", password);
					session.setAttribute("access", "user");

					UserDTO userData = userService.getByUsername(username);
					session.setAttribute("userId", userData.getId());
					res.getWriter().append("YOU LOGGED IN");
					res.setStatus(200);
				} else {
					res.getWriter().append("INCORRECT LOGIN");
					res.setStatus(401);
				}
				logger.debug("LOGIN ATTEMPT made at " + req.getRequestURI());

			} catch (JsonProcessingException e) {
				res.setStatus(400);
				e.printStackTrace();
			} catch (SQLException e) {
				res.setStatus(400);
				e.printStackTrace();
			}
		}
	}
}