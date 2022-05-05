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
    	
    	
    	String url = "jdbc:mysql://localhost/final_project"; 
    	String user = "root"; 
    	String pwd = "root";  //your secret database pwd
    	
    	String display = "<h1 style=\"text-align:center; margin-top: 20px;\">My Profile</h1>";
    	
    	Cookie[] cookies = request.getCookies();
		int idx = 0;
		boolean found = false;
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].getValue());
	  			if((cookies[i].getName()).trim().equals("ck_name")) {
	  				found = true;
	  				break;
	  			}
	  			idx++;
	  		}
		}
		String userName = cookies[idx].getValue().replace('=', ' ');	
    	
    	String sql = "SELECT age, gender, budget, min_roommate_age, max_roommate_age, housing_style, biography, email "
            			+ "FROM user_info "
            			+ "WHERE full_name = ? ";
    		
    		try (Connection conn = DriverManager.getConnection(url, user, pwd);
        			PreparedStatement ps = conn.prepareStatement(sql);) {
    			ps.setString(1, userName);
        		ResultSet rs= ps.executeQuery();
        		while(rs.next()) {	
        			
        			display += "<div class=\"container\" style=\"border: solid 2px; border-color: white;\">"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Name: " + userName+ "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Age: " + rs.getInt("age") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Gender: " + rs.getString("gender") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Budget: " + rs.getInt("budget") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Minimum Roommate Age: " + rs.getInt("min_roommate_age") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Maximum Roommate Age: " + rs.getInt("max_roommate_age") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Housing style: " + rs.getString("housing_style") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Biography: " + rs.getString("biography") + "</h3>"
        					+ "<h3 style=\"text-align:center; margin-top: 2%\">Email: " + rs.getString("email") + "</h3>"
        					+ "</div>";
        		}
        		System.out.println(display);
        		
        		
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