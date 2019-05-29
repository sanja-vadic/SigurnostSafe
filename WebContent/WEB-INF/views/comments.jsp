<%@page import="net.etfbl.sanja.dto.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Comments - safe</title>
</head>
<body>
	<form action="CommentServlet" method="POST">
		<textarea rows="4" cols="80" name="content"></textarea>
		<input type="submit" value="Comment">
	</form>
	<% List<Comment> comments = (List<Comment>) request.getAttribute("comments"); 
	   if (comments == null) comments = new ArrayList();
	   for (Comment comment : comments) {
	%>
		<p><%= comment.getContent() %></p>
	<% } %>
	
</body>
</html>