import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Util.Helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class RegisterDispatcher
 */

@WebServlet("/EditProfileDispatcher")
public class EditProfileDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public EditProfileDispatcher() {
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    	}
    	catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	
		// input names for EditProfileDispatcher
		// budget, minAge, maxAge, housingSelect, profileBio

    	String housingSelect = null;
    	String budget = null;
    	int budge = 0;
    	String roommateMinAge = null;
    	int minAge = 1000;
    	String roommateMaxAge = null;
    	int maxAge = 1000;
    	String profileBio = null;
    	
    	housingSelect = request.getParameter("housingSelect");
    	budget = request.getParameter("budget");
    	roommateMinAge = request.getParameter("minAge");
    	roommateMaxAge = request.getParameter("maxAge");
    	profileBio = request.getParameter("profileBio");
    	
    	String errorMessage = "<p style=\"background-color:#FFCCCB;text-align:center;padding: 15px;\">";
    	String ogerrorMessage = "<p style=\"background-color:#FFCCCB;text-align:center;padding: 15px;\">";
    	
    	PrintWriter out = response.getWriter();
    	
    	//VALIDATION
    	
    	if(housingSelect == null || housingSelect.length() == 0 || housingSelect.equals(" ")) {
    		errorMessage += "Housing style is missing. ";
    	}
    	
    	if(budget == null || budget.length() == 0 || budget.equals(" ")) {
    		errorMessage += "Budget is missing. ";
    	}
    	else {
    		try {
                budge = Integer.parseInt(budget);
            } catch (NumberFormatException nfe) {
                errorMessage += "Budget is invalid. ";
            }
    	}
    	
    	if(roommateMinAge == null || roommateMinAge.length() == 0 || roommateMinAge.equals(" ")) {
    		errorMessage += "Minimum roommate age is missing. ";
    	}
    	else {
    		try {
                minAge = Integer.parseInt(roommateMinAge);
            } catch (NumberFormatException nfe) {
                errorMessage += "Minimum roommate age is invalid. ";
            }
    	}
    	
    	if(roommateMaxAge == null || roommateMaxAge.length() == 0 || roommateMaxAge.equals(" ")) {
    		errorMessage += "Maximum roommate age is missing. ";
    	}
    	else {
    		try {
                maxAge = Integer.parseInt(roommateMaxAge);
            } catch (NumberFormatException nfe) {
                errorMessage += "Maximum roommate age is invalid. ";
            }
    	}
    	if(maxAge - minAge < 0) {
    		errorMessage += "Maximum roommate age cannot be less than minimum roommate age. ";
    	}
    	if(minAge < 18) {
    		errorMessage += "Must have roommates 18 or older. ";
    	}
    	if(maxAge < 18) {
    		errorMessage += "Must have roommates 18 or older. ";
    	}
    	
    	if(profileBio == null || profileBio.length() == 0 || profileBio.equals(" ")) {
    		errorMessage += "Profile bio is missing. ";
    	}
    	
    	
    	if(!errorMessage.equals(ogerrorMessage)) {
    		errorMessage += "</p>";
    		out.println(errorMessage);
    		request.getRequestDispatcher("/profile.jsp").include(request, response);
    		return;
    	}
    	
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

		String userEmail = cookies[idx].getValue();	

    	String url = "jdbc:mysql://localhost/final_project"; 
    	String user = "root"; 
    	String pwd = "root";  //your secret database pwd
    	
    	//UPDATING user info
		// budget, minAge, maxAge, housingSelect, profileBio
    	String sql = "UPDATE user_info SET budget=?, min_roommate_age=?, max_roommate_age=?, housing_style=?, biography=? WHERE email=?;";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(sql);) {
    		ps.setInt(1, budge);
    		ps.setInt(2, minAge);
    		ps.setInt(3, maxAge);
    		ps.setString(4, housingSelect);
    		ps.setString(5, profileBio);
    		ps.setString(6, userEmail);
    		int row = ps.executeUpdate();
    	}
    	catch (SQLException ex) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    		
    	response.setContentType("text/html");
    	response.sendRedirect("index.jsp");
    	
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
