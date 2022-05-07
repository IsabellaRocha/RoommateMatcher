import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        
    	String userID = null;
    	String otherID = null;
    	
    	userID = request.getParameter("userID");
    	otherID = request.getParameter("otherID");
    	
    	
    	String url = "jdbc:mysql://localhost/final_project"; 
    	String user = "root"; 
    	String pwd = "root";  //your secret database pwd
    	
    	String sql = "INSERT INTO response_table(user_id, other_id, choice) VALUES (?, ?)"
    	try Connection conn = DriverManager.getConnection(url, user, pwd);
    			PreparedStatement ps = conn.prepareStatement(sql);) {
    		ps.setString(1, userID);
    		ps.setString(2, otherID);
    		ps.setBoolean(3, true);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		catch (SQLException ex ) {
    			System.out.println("SQLException" + ex.getMessage());
    		}
    	}
    			
    }
       	
        
        
}