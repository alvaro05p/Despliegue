package com.shop;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CustomerRegistrationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        // Register form
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Client Register</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        // Bootstrap classes for a cleaner page
        out.println("<div class='container mt-5'>");
        out.println("<h2>Client Register</h2>");
        out.println("<form method='post' action='register'>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='username' class='form-label'>Username:</label>");
        out.println("<input type='text' class='form-control' name='username' id='username' required/><br/>");
        out.println("</div>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='password' class='form-label'>Password:</label>");
        out.println("<input type='password' class='form-control' name='password' id='password' required/><br/>");
        out.println("</div>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='email' class='form-label'>Email:</label>");
        out.println("<input type='email' class='form-control' name='email' id='email' required/><br/>");
        out.println("</div>");
        
        out.println("<button type='submit' class='btn btn-primary'>Register</button>");
        out.println("</form>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        // Process the client register
        String username = request.getParameter("username");

        //In real production will be better to hash the password
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
              "INSERT INTO Customers (username, password, email) VALUES (?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int customerId = rs.getInt(1);
                // Save the user id in a cookie
                Cookie cookie = new Cookie("customerId", String.valueOf(customerId));
                cookie.setMaxAge(60*60*24*365);
                response.addCookie(cookie);
            }
            response.sendRedirect("shop");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
