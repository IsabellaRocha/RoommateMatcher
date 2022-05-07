import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.xdevapi.Statement;

import javax.servlet.ServletException;


@WebServlet("/MatchedDispatcher")
public class MatchedDispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
        
    	int userID = 0;
    	String otherID = null;
    	String email = null;
    	email = request.getParameter("email");
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
    	
    	
    	String userEmail = cookies[idx].getValue();
    	otherID = request.getParameter("other_id");
    	String query = "SELECT user_id FROM user_info WHERE email = ? ";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(query);) {
    		
    		ps.setString(1, userEmail);    		
			ResultSet rs = ps.executeQuery();
			rs.next();
			userID = rs.getInt("user_id");
    	}
    	
    	catch (SQLException ex ) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    			
    		
    	String sql = "INSERT INTO response_table(user_id, other_id, choice) VALUES (?, ?, ?)";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(sql);) {
    		ps.setInt(1, userID);
    		ps.setString(2, otherID);
    		ps.setBoolean(3, true);
    		
    		int row = ps.executeUpdate();
    	}
    		catch (SQLException ex ) {
    			System.out.println("SQLException" + ex.getMessage());
    		}
    	// Search for match requests SENT FROM other people
    	String sql1 = "SELECT user_id FROM response_table WHERE other_id = ? AND choice = true";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
        		PreparedStatement ps = conn.prepareStatement(sql1);) {
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
                		String sql3 = "INSERT INTO matches_table(user_id,other_id) VALUES (?,?), (?,?)";
                    	try (Connection conn3 = DriverManager.getConnection(url, user, pwd);
                        		PreparedStatement ps3 = conn3.prepareStatement(sql3);) {
                    		ps3.setInt(1, userID);
                    		ps3.setInt(2, rs2.getInt("user_id"));
                    		ps3.setInt(3, rs2.getInt("user_id"));
                    		ps3.setInt(4, userID);
                        	int row = ps3.executeUpdate();
                    	}
                    	catch (SQLException ex ) {
                    		System.out.println("SQLException" + ex.getMessage());
                    		ex.printStackTrace();
                    	}
                	}
            	}
            	catch (SQLException ex ) {
            		System.out.println("SQLException" + ex.getMessage());
            		ex.printStackTrace();
            	}
        	}
    	}
    	catch (SQLException ex ) {
    		System.out.println("SQLException" + ex.getMessage());
    		ex.printStackTrace();
    	}
    	
    	response.setContentType("text/html");
    	response.sendRedirect("OptionsDispatcher");
    			
       	
    }  
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
        
}
