<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Registration Page Safe</title>
	<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<h3>Safe application</h3>
	<form action="RegistrationServlet" method="POST">
		<input type="text" name="firstname" placeholder="Firstname..." id="registrationFirstname"><br />
		<input type="text" name="lastname" placeholder="Lastname..." id="registrationlastname"><br />
		<input type="text" name="username" placeholder="Username..." id="registrationUsername"><br />
		<input type="password" name="password" placeholder="Password..." id="registrationPassword"><br />
		<input type="text" name="age" placeholder="Age..." id="registrationAge"><br />
		<input type="submit">
	</form>
</body>
</html>