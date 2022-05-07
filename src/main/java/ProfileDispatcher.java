import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Servlet implementation class SearchDispatcher
 */

@WebServlet("/ProfileDispatcher")
public class ProfileDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ProfileDispatcher() {
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //TODO check if you've done the initialization
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    	
    	PrintWriter out = response.getWriter();
    	String url = "jdbc:mysql://localhost/final_project"; 
    	String user = "root"; 
    	String pwd = "root";  //your secret database pwd
    	
    	String display = "<h1 style=\"text-align:center; margin-top: 20px;\">My Profile</h1>";
		display += "<p style=\"text-align:center; margin-top: 0px;\"> click edit profile to edit profile info </p>";
    	
    	Cookie[] cookies = null;
    	cookies = request.getCookies();
    	
		int idx = 0;
		boolean found = false;
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].getValue());
	  			if((cookies[i].getName()).trim().equals("ck_email")) {
	  				found = true;
	  				break;
	  			}
	  			idx++;
	  		}
		}
		if(!found) {
			String errorMessage = "<p style=\"background-color:#FFCCCB;text-align:center;padding: 15px;\">"
        			+ "Please log in or register before trying to view profile.</p>";
			out.println(errorMessage);
    		request.getRequestDispatcher("/index.jsp").include(request, response);
    		return;
		}
		String userEmail = cookies[idx].getValue();	
    	
    	String sql = "SELECT age, gender, budget, min_roommate_age, max_roommate_age, housing_style, biography, full_name "
            			+ "FROM user_info "
            			+ "WHERE email = ? ";
    		
    		try (Connection conn = DriverManager.getConnection(url, user, pwd);
        			PreparedStatement ps = conn.prepareStatement(sql);) {
    			ps.setString(1, userEmail);
        		ResultSet rs= ps.executeQuery();
        		while(rs.next()) {	

					// input names for EditProfileDispatcher
					// budget, minAge, maxAge, housingSelect, profileBio

					display += "<div class=\"container\" style=\"border: solid 2px; border-color: white;\">"
        			+ "<form action=\"EditProfileDispatcher\" method=\"GET\"></form>"
           			+ "<h4 style=\"text-align:center; margin-top: 2%;\">  Name:" + rs.getString("full_name") +"</h4>"
            		+ "<h4 style=\"text-align:center; margin-top: 2%;\">  Age: "+ rs.getInt("age") +" </h4>"
            		+ "<h4 style=\"text-align:center; margin-top: 2%;\">  Gender: "+ rs.getString("gender") +" </h4>"
            		+ "<div class=\"row\" style=\"text-align:right; margin-top: 2%;\">"
                	+ "<div class=\"col-6\">"
                    + "<h4 style=\"text-align:right;\">  Budget: </h4>"
                	+ "</div>"
                	+ "<div class=\"col-6\">"
                    + "<div class=\"input-group\" style=\"font-size: 16px; width: 180px; text-align:left;\">"
                    + "<div class=\"input-group-prepend\">"
                    + "<div class=\"input-group-text\" style=\"height: 35px;\">$</div>"
                    + "</div>"
                    + "<input name=\"budget\" type=\"number\" class=\"form-control\" id=\"inlineFormInputGroupUsername\" placeholder=\"Budget\" value=\""+ rs.getInt("budget") +"\" style=\"height: 35px;\" required>"
                    + "<div class=\"input-group-append\">"
                    + "<span class=\"input-group-text\" style=\"height: 35px;\">.00</span>"
                    + "</div>"
                    + "</div>"
                	+ "</div>"
            		+ "</div>"
            		+ "<div class=\"row\">"
                	+ "<div class=\"col-7\">"
                    + "<h4 style=\"text-align:right; margin-top: 2%;\">  Minimum Roommate Age: </h4>"
                	+ "</div>"
                	+ "<div class=\"col-5\">"
                    + "<h4 style=\"text-align:left; margin-top: 2%;\">"
                    + "<input name=\"minAge\" type=\"number\" class=\"form-control\" id=\"roommateMinAge\" placeholder=\"Enter minimum age\" style=\"font-size: 16px; width: 100px; height: 35px;\" value=\""+ rs.getInt("min_roommate_age") +"\" required>"
                    + "</h4>"
                	+ "</div>"
            		+ "</div>"
            		+ "<div class=\"row\">"
                	+ "<div class=\"col-7\">"
                    + "<h4 style=\"text-align:right; margin-top: 2%;\">  Maximum Roommate Age: </h4> </div>"
                	+ "<div class=\"col-5\">"
                    + "<h4 style=\"text-align:left; margin-top: 2%;\">"
                    + "<input name=\"maxAge\" type=\"number\" class=\"form-control\" id=\"roommateMaxAge\" placeholder=\"Enter maximum age\" style=\"font-size: 16px; width: 100px; height: 35px;\" value=\"" + rs.getInt("max_roommate_age") + "\" required>"
                    + "</h4>"
                	+ "</div>"
            		+ "</div>"
            		+ "<h4 style=\"text-align:center; margin-top: 2%;\">  Housing style: "
                	+ "<select name=\"housingSelect\" class=\"custom-select form-select-sm\" id=\"housingSelect\" style=\"font-size: 16px; width: 250px;\">"
                    + "<option selected> "+ rs.getString("housing_style") +" </option>"
                    + "<option value=\"Apartment\">Apartment</option>"
                    + "<option value=\"House\">House</option>"
                    + "<option value=\"Townhouse\">Townhouse</option>"
                    + "<option value=\"Loft\">Loft</option>"
					+ "</select>"
					+ "</h4>"
            		+ "<h4 style=\"text-align:center; margin-top: 2%;\">  Biography:</h4>"
                	+ "<div style=\"align-items: center; margin-left: auto; margin-right: auto; width: 400px;\">"
                    + "<textarea name=\"profileBio\" class=\"form-control\" style=\"height: 100px; width: 100%; padding: 10px;\">"+rs.getString("biography")+"</textarea>"
                	+ "</div>"
            		+ "<h4 style=\"text-align:center; margin-top: 2%;\">  Email: "+userEmail+" </h4>"
            		+ "<div style=\"align-items: center; margin-left: auto; margin-right: auto; width: 100px; margin-top: 5%;\">"
                	+ "<button class=\"btn btn-primary\" type=\"submit\">Submit Changes</button>"
            		+ "</div>"
        			+ "</form>"
    				+ "</div>"
					+ "";
        		}        		
        		
    		}
    		catch (SQLException ex) {
        		System.out.println("SQLException " + ex.getMessage() + sql);
        	}
    		request.setAttribute("display", display);
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/profile.jsp"); 
    		dispatcher.forward(request, response); 
    	}

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}