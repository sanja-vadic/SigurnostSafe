package net.etfbl.sanja.servlets;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

/**
 * Servlet implementation class RandomNumberServlet
 */
@WebServlet("/RandomNumberServlet")
public class RandomNumberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RandomNumberServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numberOfDigits = Integer.parseInt(request.getParameter("number"));
		
		if(numberOfDigits < 0 || numberOfDigits > 10) {
			response.getWriter().println("Number of digits must be in range [0 - 10].");
			return;
		}
		
		int[] randomNumberDigits = new int[10];
		for(int i=0; i < numberOfDigits; ++i) {
			randomNumberDigits[i] = new Random().nextInt(10);
		}
		
		StringBuilder result = new StringBuilder();
		for(int i=0; i < numberOfDigits; ++i) {
			result.append(randomNumberDigits[i]);
		}
		
		response.getWriter().println("Number is: " + result.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
