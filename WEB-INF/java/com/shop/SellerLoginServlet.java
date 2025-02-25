package com.shop;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class SellerLoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Error message if autentication fails
        String errorMessage = request.getParameter("error") != null ? 
            "<div class='alert alert-danger'>Invalid username or password</div>" : "";

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Seller Login</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head>");
        out.println("<body class='d-flex justify-content-center align-items-center vh-100 bg-light'>");
        out.println("<div class='container'>");
        out.println("<div class='row justify-content-center'>");
        out.println("<div class='col-md-4'>");
        out.println("<div class='card shadow p-4'>");
        out.println("<h2 class='text-center'>Seller Login</h2>");
        out.println(errorMessage);
        out.println("<form method='post' action='sellerLogin'>");
        out.println("<div class='mb-3'>");
        out.println("<label for='username' class='form-label'>Username</label>");
        out.println("<input type='text' class='form-control' id='username' name='username' required>");
        out.println("</div>");
        out.println("<div class='mb-3'>");
        out.println("<label for='password' class='form-label'>Password</label>");
        out.println("<input type='password' class='form-control' id='password' name='password' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-primary w-100'>Login</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");
        out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // An easy query
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT id FROM Sellers WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int sellerId = rs.getInt("id");
                HttpSession session = request.getSession();
                session.setAttribute("sellerId", sellerId);
                response.sendRedirect("sellerDashboard");
            } else {
                response.sendRedirect("sellerLogin?error=1");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
