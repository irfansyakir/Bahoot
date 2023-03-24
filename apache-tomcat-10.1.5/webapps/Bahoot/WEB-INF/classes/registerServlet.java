import java.io.*;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.logging.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.*; // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


@WebServlet("/register")
public class registerServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(registerServlet.class.getName());
    static String nameStr;
    static String emailStr;
    static String passwordStr;
    static String phoneNumberStr;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.info("registerServlet Called"); 

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        
        nameStr = request.getParameter("name");
        emailStr = request.getParameter("email");
        passwordStr = request.getParameter("password");
        phoneNumberStr = request.getParameter("phoneNumber");
        
        if(verifyEmail() && verifyPhoneNumber())
            registerToDb(response);
        else if (!verifyEmail())
            out.println("This email address is taken, please try again.");
        else if (!verifyPhoneNumber())
            out.println("This phone number is taken, please try again.");

    }

    private static void registerToDb(HttpServletResponse response) {
        // register user
        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Bahoot?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "root", "password");

                Statement stmt = conn.createStatement();) {
            
            String sqlStrRegister = "INSERT INTO Users Values (null, '" + emailStr + "', '" + nameStr + "', '" + passwordStr
                    + "', '" + phoneNumberStr + "')";
            LOGGER.info(sqlStrRegister); // Add a logging statement
            stmt.executeUpdate(sqlStrRegister);
            
        } catch (SQLException e) {

            LOGGER.info("SQL Failed" + e); // Add a logging statement
        }

    }

    private static boolean verifyEmail() {
        // check if email exists
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Bahoot?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "root", "password");

            Statement stmt = conn.createStatement();) {
                int id = 0;
                String sqlStrEmail = "SELECT * FROM users WHERE email ='" + emailStr + "'";
                ResultSet rsetEmail = stmt.executeQuery(sqlStrEmail);

                if (!rsetEmail.next()) 
                    return true;  
                else 
                    return false;
            } catch (SQLException e) {

                LOGGER.info("SQL Failed" + e); // Add a logging statement
            }
        return false;
    }

    private static boolean verifyPhoneNumber() {
        // check if phone number exists
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Bahoot?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "root", "password");

            Statement stmt = conn.createStatement();) {
                int id = 0;
                String sqlStrPhoneNumber = "SELECT * FROM users WHERE mobile_number ='" + phoneNumberStr + "'";
                ResultSet rsetPhoneNumber = stmt.executeQuery(sqlStrPhoneNumber);

                if (!rsetPhoneNumber.next())
                    return true;
                else 
                    return false;
            } catch (SQLException e) {

                LOGGER.info("SQL Failed" + e); // Add a logging statement
            }
        return false;
    }
    

        

}
