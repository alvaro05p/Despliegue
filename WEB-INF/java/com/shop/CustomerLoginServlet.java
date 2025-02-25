package com.shop;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CustomerLoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        // Login form
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Client Login</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        out.println("<div class='container mt-5'>");
        out.println("<h2>Client Login</h2>");
        
        // Show an error parameter if exists
        String error = request.getParameter("error");
        if (error != null && error.equals("1")) {
            out.println("<div class='alert alert-danger'>Incorrect user or password</div>");
        }
        
        out.println("<form method='post' action='login'>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='username' class='form-label'>Username:</label>");
        out.println("<input type='text' class='form-control' name='username' id='username' required/><br/>");
        out.println("</div>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='password' class='form-label'>Password:</label>");
        out.println("<input type='password' class='form-control' name='password' id='password' required/><br/>");
        out.println("</div>");
        
        out.println("<button type='submit' class='btn btn-primary'>Enter</button>");
        out.println("</form>");
        out.println("<a href='register' class='btn btn-link mt-3'>Don´t have an account? Register here</a>");
        
        out.println("</div>");
        
        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        //Search for a customer with the actual parameters username and password
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT id FROM Customers WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("id");
                HttpSession session = request.getSession();
                session.setAttribute("customerId", customerId);

                // Save a persistent cookie
                Cookie cookie = new Cookie("customerId", String.valueOf(customerId));

                //One year duration
                cookie.setMaxAge(60*60*24*365);
                response.addCookie(cookie);
                response.sendRedirect("shop");
            } else {
                response.sendRedirect("login?error=1");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
