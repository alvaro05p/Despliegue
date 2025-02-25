package com.shop;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class PurchaseHistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        int customerId = 0;
        HttpSession session = request.getSession();

        //If session is null, (no login)
        if (session.getAttribute("customerId") != null) {
            customerId = (Integer) session.getAttribute("customerId");
        } else {

            //Getting all cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {

                //Iterate cookies
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("customerId")) {
                        customerId = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (customerId == 0) {
            response.sendRedirect("login");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Purchase history</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        out.println("<div class='container mt-5'>");
        out.println("<h2 class='mb-4'>Purchase history</h2>");
        
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
              "SELECT trackingNumber, total, shipping, purchaseDate FROM Purchases WHERE customerId = ?");
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            // Purchase table
            out.println("<table class='table table-striped table-bordered'>");
            out.println("<thead class='thead-dark'><tr><th>Tracking number</th>" +
                        "<th>Total</th><th>Shipping</th><th>Date</th></tr></thead>");
            out.println("<tbody>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("trackingNumber") + "</td>");
                out.println("<td>" + rs.getDouble("total") + "€</td>");
                out.println("<td>" + rs.getDouble("shipping") + "€</td>");
                out.println("<td>" + rs.getTimestamp("purchaseDate") + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
        } catch (Exception e) {
            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
        }
        
        // Buuton to go back to the shop
        out.println("<a href='shop' class='btn btn-primary mt-3'>Go back to the shop</a>");
        
        out.println("</div>");
        out.println("</body></html>");
    }
}
