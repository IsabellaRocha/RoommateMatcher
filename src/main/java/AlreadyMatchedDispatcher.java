
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

@WebServlet("/AlreadyMatchedDispatcher")
public class AlreadyMatchedDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AlreadyMatchedDispatcher() {
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
    	
    	String display = "";
    	int userID = 0;
    	String userEmail = "";
    	Cookie[] cookies = null;
    	cookies = request.getCookies();
    	
    	int idx = 0;
		boolean found = false;
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
	  			if((cookies[i].getName()).trim().equals("ck_email")) {
	  				found = true;
	  				break;
	  			}
	  			idx++;
	  		}
		}
		
		if(!found) {
			String errorMessage = "<p style=\"background-color:#FFCCCB;text-align:center;padding: 15px;\">"
        			+ "Please log in or register before trying to match.</p>";
			out.println(errorMessage);
    		request.getRequestDispatcher("/index.jsp").include(request, response);
    		return;
		}
    	
    	userEmail = cookies[idx].getValue();
    	// set the userID from the cookie
    	String query = "SELECT user_id FROM user_info WHERE email = ? ";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(query);) {
    		ps.setString(1, userEmail);    		
			ResultSet rs1 = ps.executeQuery();
			rs1.next();
			userID = rs1.getInt("user_id");
    	}
    	catch (SQLException ex ) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    	// Search for match requests SENT FROM other people
    	String sql = "SELECT user_id FROM response_table WHERE other_id = ? AND choice = true";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
        		PreparedStatement ps = conn.prepareStatement(sql);) {
    		ps.setInt(1, userID);
        	ResultSet rs2 = ps.executeQuery();
        	
        	while(rs2.next()) {
        		// Search for if you sent match request back to respective person
        		String sql2 = "SELECT choice FROM response_table WHERE other_id = ? AND user_id = ?";
            	try (Connection conn2 = DriverManager.getConnection(url, user, pwd);
                		PreparedStatement ps2 = conn2.prepareStatement(sql2);) {
            		ps2.setInt(1, rs2.getInt("user_id"));
            		ps2.setInt(2, userID);
                	ResultSet rs3= ps2.executeQuery();
                	if (rs3.next() == true)
                		// rs3 is not empty
                	{
                		// if both searches go through, insert match entry into matches table
                		String sql3 = "INSERT INTO matches_table(user_id,other_id) VALUES (?,?)";
                    	try (Connection conn3 = DriverManager.getConnection(url, user, pwd);
                        		PreparedStatement ps3 = conn3.prepareStatement(sql3);) {
                    		ps3.setInt(1, userID);
                    		ps3.setInt(2, rs2.getInt("user_id"));
                        	int row = ps3.executeUpdate();
                    	}
                    	catch (SQLException ex ) {
                    		System.out.println("SQLException" + ex.getMessage());
                    	}
                	}
            	}
            	catch (SQLException ex ) {
            		System.out.println("SQLException" + ex.getMessage());
            	}
        	}
    	}
    	catch (SQLException ex ) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    	// Search through matches table for all of your matches
    	String sql4 = "SELECT other_id FROM matches_table WHERE user_id = ?";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
        		PreparedStatement ps4 = conn.prepareStatement(sql4);) {
    		ps4.setInt(1, userID);
    		ResultSet rs4 = ps4.executeQuery();
    		while (rs4.next())
    		{
    			// Search for all the info about your matched person i
	    		String sql5 = "SELECT * FROM user_info WHERE user_id = ?";
	        	try (Connection conn2 = DriverManager.getConnection(url, user, pwd);
	            		PreparedStatement ps5 = conn2.prepareStatement(sql5);) {
	        		ps5.setInt(1, rs4.getInt("other_id"));
	        		ResultSet rs5 = ps5.executeQuery();
	        		
            		String gender = rs5.getString("gender");
            		String image = "";
            		if(gender.equals("female")) {
            			image = "female.png";
            		}
            		else if(gender.equals("male")) {
            			image = "male.png";
            		}
            		display += "<div class=\"col-lg-3\">"
            				+ "            <div class=\"poster\">"
            				+ "                <img class=\"photo\" src=\"" + image + "\">"
            				+ "                <div class=\"metrics\">"
            				+ "                    <p>Age: " + rs5.getInt("age") + "</p>"
            				+ "                    <p>Housing Style: " + rs5.getString("housing_style") + "</p>"
            				+ "                    <p>About me: "+ rs5.getString("biography") + "</p>"
            				+ "                </div>"
            				+ "            </div>"
            				+ "            <div class=\"info\">"
            				+ "                <p>"+ rs5.getString("full_name") + "</p>"
            				+ "                <p>Budget: "+ rs5.getInt("budget") + "</p>"
            				+ "            </div>"
            				+ "        </div>";
	        	}
	        	catch (SQLException ex ) {
	        		System.out.println("SQLException" + ex.getMessage());
	        	}
        	}
    	}
    	catch (SQLException ex ) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    	request.setAttribute("display", display);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/matching.jsp"); 
		dispatcher.forward(request, response); 
    }
    	
    	
    	
    	
    			
        // TODO

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
