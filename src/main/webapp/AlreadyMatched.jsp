<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="matchingStyle.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>RoomMatch</title>
</head>
<body>
    <div class="navbar">
		<img src="puzzle.png" alt="Logo">
		
		<a class="active" href="index.jsp">Home</a>
		<a href="about.html">About Us</a>
		<a href="ProfileDispatcher">Profile</a>
		<a href="AlreadyMatchedDispatcher">Match History</a>
		<a href="OptionsDispatcher">Get Matching!</a>
		
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

    <div class="container-fluid">
        <div class ="row flex-wrap" id="bruh">
        <% out.println(request.getAttribute("display")); %>
        </div>
        <div id="empty">
        </div>
    </div>

</body>
</html>