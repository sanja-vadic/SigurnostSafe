package net.etfbl.sanja.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.etfbl.sanja.dao.UserDAO;
import net.etfbl.sanja.dto.User;
import net.etfbl.sanja.ids.IDSManager;
import net.etfbl.sanja.mysql.UserMySql;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> map = new HashMap<>();
		
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		int age = Integer.parseInt(request.getParameter("age"));

		map.put("firstname", firstname);
		map.put("lastname", lastname);
		map.put("username", username);
		map.put("age", Integer.toString(age));

		boolean parameterTampering = true;

		Iterator<Map.Entry<String, String>> iterator = null;
		for (iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, String> parameterEntry = iterator.next();
			String parameterKey = new String(parameterEntry.getKey());
			String parameterValue = new String(parameterEntry.getValue());

			parameterTampering = IDSManager.checkParameterTampering(parameterKey, parameterValue, getServletContext());
		}

		if (!parameterTampering) {
			UserDAO userDAO = new UserMySql();
			boolean registrationSuccess = userDAO.insert(User.builder().firstname(firstname).lastname(lastname)
					.username(username).password(password).age(age).build());

			if (registrationSuccess) {
				response.getWriter().println("Registration success.");
			} else {
				response.getWriter().println("Registration failed.");
			}
		} else {
			response.getWriter().println("Your input is recognized as Parameter Tampering attack.");
		}
	}

}
