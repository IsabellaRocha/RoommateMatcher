<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
    
<html>

<head>
    <meta charset="utf-8">
    <title>Home Page</title>
    <link href="style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
	<div class="navbar">
		<img src="puzzle.png" alt="Logo">
		
		<a class="active" href="index.jsp">Home</a>
		<a href="about.html">About Us</a>
		<a href="ProfileDispatcher">Profile</a>
		<a href="#history">Match History</a>
		<a href="#matching">Get Matching!</a>
		
		<%Cookie[] cookies= null; 
			cookies = request.getCookies();
  			int idx = 0;
  			boolean found = false;
  			if(cookies != null) {
  				for(int i = 0; i < cookies.length; i++) {
  	  				if((cookies[i].getName()).trim().equals("ck_name")) {
  	  					found = true;
  	  					break;
  	  				}
  	  				idx++;
  	  			}
  			}
  			%>
  			<%if(!found) {%>
  			<form action="userLogin.html">
  			<button type="submit">Login</button>
  			</form>
  			<%} %>
  			<%if(found) {%>
  				<p>Hi, <%out.print(cookies[idx].getValue().replace('=', ' ')); %> </p>
  				<form action="LogoutDispatcher" method="GET">
  				<button type="submit">Logout</button>
  				</form>
  			<%} %>
	</div>
	
	<div class="main">
		<div class="title">
			<h1>
				Welcome to RoomMatch
			</h1>
		</div>
		<div class="title">
			<img src="puzzle.png" alt="Logo">
		</div>
		<br>
		<h3>
			We're on a mission to help students find their
		</h3>
		<h3>
			ideal roommate matches!
		</h3>
		<br><br>
		<%if(!found) {%>
		<form action="userLogin.html">
		<button type="submit">Get Matching!</button>
		</form>
		<%} %>
		<%if(found) {%>
		<form action="matching.html">
		<button type="submit">Get Matching!</button>
		</form>
		<%} %>
	</div>
</body>
</html>