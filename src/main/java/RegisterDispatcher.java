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

@WebServlet("/RegisterDispatcher")
public class RegisterDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public RegisterDispatcher() {
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
    	String email = null;
    	String password = null;
    	String fullName = null;
    	String personalAge = null;
    	int age = 1000;
    	String phone = null;
    	String housingSelect = null;
    	String budget = null;
    	int budge = 0;
    	String roommateMinAge = null;
    	int minAge = 1000;
    	String roommateMaxAge = null;
    	int maxAge = 1000;
    	String gender = null;
    	String profileBio = null;
    	
    	email = request.getParameter("email");
    	password = request.getParameter("password");
    	fullName = request.getParameter("fullName");
    	personalAge = request.getParameter("personalAge");
    	phone = request.getParameter("phone");
    	housingSelect = request.getParameter("housingSelect");
    	budget = request.getParameter("budget");
    	roommateMinAge = request.getParameter("roommateMinAge");
    	roommateMaxAge = request.getParameter("roommateMaxAge");
    	gender = request.getParameter("gender");
    	profileBio = request.getParameter("profileBio");
    	
    	
    	String errorMessage = "<p style=\"background-color:#FFCCCB;text-align:center;padding: 15px;\">";
    	String ogerrorMessage = "<p style=\"background-color:#FFCCCB;text-align:center;padding: 15px;\">";
    	
    	PrintWriter out = response.getWriter();
    	
    	//VALIDATION    	
    	if(Helper.emailAlreadyRegistered(email, request, response)) {
    		errorMessage += "User with this email is already registered. Please log in.</p>";
    		out.println(errorMessage);
    		request.getRequestDispatcher("/userLogin.html").include(request, response);
    		return;
    	}
    	
    	if(fullName == null || fullName.length() == 0 || fullName.equals(" ") || !Helper.validName(fullName)) {
    		errorMessage += "Name is missing or invalid. ";
    	}
    	
    	if(email == null || email.length() == 0 || email.equals(" ") || email.indexOf("@") == -1 || !Helper.isValidEmail(email)) {
    		errorMessage += "Email is missing or invalid. ";
    	}
    	
    	if(password == null || password.length() == 0 || password.equals(" ")) {
    		errorMessage += "Password is missing or invalid. ";
    	}
    	
    	if(personalAge == null || personalAge.length() == 0 || personalAge.equals(" ")) {
    		errorMessage += "Age is missing. ";
    	}
    	else {
    		try {
                age = Integer.parseInt(personalAge);
            } catch (NumberFormatException nfe) {
                errorMessage += "Age is invalid. ";
            }
    	}
    	if(age < 18) {
    		errorMessage += "Must be 18 or older. ";
    	}
    	
    	//ADD REGEX CHECKER FOR THIS    	
    	if(phone == null || phone.length() == 0 || phone.equals(" ")) {
    		errorMessage += "Phone number is missing. ";
    	}
    	if(phone.length() < 10) {
    		errorMessage += "Invalid phone number. Please submit in format 9176574879";
    	}
    	
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
    	
    	if(gender == null || gender.length() == 0 || gender.equals(" ")) {
    		errorMessage += "Gender is missing. ";
    	}
    	
    	if(profileBio == null || profileBio.length() == 0 || profileBio.equals(" ")) {
    		errorMessage += "Profile bio is missing. ";
    	}
    	
    	    	
    	if(!errorMessage.equals(ogerrorMessage)) {
    		errorMessage += "</p>";
    		out.println(errorMessage);
    		request.getRequestDispatcher("/userRegistration.html").include(request, response);
    		return;
    	}
    	
    	String url = "jdbc:mysql://localhost/final_project"; 
    	String user = "root"; 
    	String pwd = "root";  //your secret database pwd
    	
    	//Inserting base log in info
    	String sql = "INSERT INTO login_info(email, password) VALUES (?, ?)";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(sql);) {
    		ps.setString(1, email);
    		ps.setString(2, password);
    		int row = ps.executeUpdate();

    	}
    	catch (SQLException ex) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    	
    	//Inserting all other info
    	String sql2 = "INSERT INTO user_info(age, full_name, gender, budget, min_roommate_age, max_roommate_age, housing_style, biography, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	try (Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(sql2);) {
    		ps.setString(1, personalAge);
    		ps.setString(2, fullName);
    		ps.setString(3,  gender);
    		ps.setInt(4, budge);
    		ps.setInt(5, minAge);
    		ps.setInt(6, maxAge);
    		ps.setString(7, housingSelect);
    		ps.setString(8, profileBio);
    		ps.setString(9, email);
    		int row = ps.executeUpdate();

    	}
    	catch (SQLException ex) {
    		System.out.println("SQLException" + ex.getMessage());
    	}
    	String new_name = request.getParameter("fullName").replace(' ', '=');
    	
    	Cookie ck = new Cookie("ck_name", new_name);
    	
    	ck.setMaxAge(60*60);
    	response.addCookie(ck);
    	
    	response.setContentType("text/html");
    	
    	response.sendRedirect("index.jsp");
    	
        //TODO
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
