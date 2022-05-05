<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en" dir="ltr">

<head>
    <meta charset="utf-8">
    <title>My Profile</title>
    <link href="profileStyle.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
	<div class="navbar">
		<img src="puzzle.png" alt="Logo">

		<a class="active" href="index.jsp">Home</a>
		<a href="#aboutus">About Us</a>
		<a href="profile.jsp">Profile</a>
		<a href="#history">Match History</a>
		<button type="button">Login</button>
	</div>

	<% out.println(request.getAttribute("display")); %>
	
</body>
</html>