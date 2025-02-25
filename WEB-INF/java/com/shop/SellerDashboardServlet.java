package com.shop;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class SellerDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("sellerId") == null) {
            response.sendRedirect("sellerLogin");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Seller Dashboard</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head>");
        out.println("<body class='bg-light'>");

        out.println("<div class='container mt-5'>");
        out.println("<div class='card shadow p-4'>");
        out.println("<h2 class='text-center mb-4'>Seller Dashboard</h2>");

        // Button to upload a new prodduct
        out.println("<div class='text-center mb-3'>");
        out.println("<a href='uploadProduct' class='btn btn-success'>Upload New Product</a>");
        out.println("</div>");

        out.println("<h3 class='mt-4'>Purchase List</h3>");

        out.println("<div class='table-responsive'>");
        out.println("<table class='table table-bordered table-striped'>");
        out.println("<thead class='table-dark'>");
        out.println("<tr>");
        out.println("<th>Tracking Number</th>");
        out.println("<th>Customer</th>");
        out.println("<th>Total (€)</th>");
        out.println("<th>Shipping (€)</th>");
        out.println("<th>Date</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
              "SELECT p.trackingNumber, c.username, p.total, p.shipping, p.purchaseDate " +
              "FROM Purchases p JOIN Customers c ON p.customerId = c.id");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("trackingNumber") + "</td>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getDouble("total") + "</td>");
                out.println("<td>" + rs.getDouble("shipping") + "</td>");
                out.println("<td>" + rs.getTimestamp("purchaseDate") + "</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td colspan='5' class='text-danger'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody>");
        out.println("</table>");
        //Table
        out.println("</div>");
        //Card
        out.println("</div>");
        //Container 
        out.println("</div>");

        // Return to home page
        out.println("<div class='text-center mt-4'>");
        out.println("<a href='home' class='btn btn-danger'>Close</a>");
        out.println("</div>");

        out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}
