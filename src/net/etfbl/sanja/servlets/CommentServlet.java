package net.etfbl.sanja.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.etfbl.sanja.dao.CommentDAO;
import net.etfbl.sanja.dto.Comment;
import net.etfbl.sanja.ids.IDSManager;
import net.etfbl.sanja.mysql.CommentMySql;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommentDAO commentDAO = new CommentMySql();
		List<Comment> comments = commentDAO.selectAll();
		request.setAttribute("comments", comments);
		request.getRequestDispatcher("WEB-INF/views/comments.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String content = request.getParameter("content");
		if (content == null) content = "";
		if (IDSManager.checkXSS(content)) {
			response.getWriter().println("Your input is recognized as XSS attack.");
			return;
		}
		
		CommentDAO commentDAO = new CommentMySql();
		boolean insertSuccess = commentDAO.insert(Comment.builder().content(content).build());
		//Sta ako ne uspije insert
		response.addCookie(new Cookie("xssCookie", Integer.toString(new Random().nextInt(100))));
		response.sendRedirect("CommentServlet");
		
	}

}
