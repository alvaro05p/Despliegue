package com.shop;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class ConfirmPurchaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        Double total = (Double) session.getAttribute("total");
        Double shipping = (Double) session.getAttribute("shipping");
        int customerId = 0;
        
        // Get client id with the session or the cookie
        if (session.getAttribute("customerId") != null) {
            customerId = (Integer) session.getAttribute("customerId");
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
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
        
        int trackingNumber = 0;
        
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            // Obtain the last tracking number an add one more
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(trackingNumber) AS maxTracking FROM Purchases");
            if (rs.next()) {
                trackingNumber = rs.getInt("maxTracking") + 1;
            } else {
                trackingNumber = 1;
            }

            // Insert the purchase in the database
            PreparedStatement ps = conn.prepareStatement(
              "INSERT INTO Purchases (customerId, trackingNumber, total, shipping, purchaseDate) VALUES (?, ?, ?, ?, NOW())",
              Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, customerId);
            ps.setInt(2, trackingNumber);
            ps.setDouble(3, total);
            ps.setDouble(4, shipping);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int purchaseId = 0;
            if (generatedKeys.next()) {
                purchaseId = generatedKeys.getInt(1);
            }

            // Insert purchase details
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                PreparedStatement psDetail = conn.prepareStatement(
                  "INSERT INTO PurchaseDetails (purchaseId, itemId, quantity, unitPrice) VALUES (?, ?, ?, ?)");
                psDetail.setInt(1, purchaseId);
                psDetail.setInt(2, entry.getKey());
                psDetail.setInt(3, entry.getValue());

                // Query for the item price
                PreparedStatement psItem = conn.prepareStatement("SELECT price FROM Items WHERE id = ?");
                psItem.setInt(1, entry.getKey());
                ResultSet rsItem = psItem.executeQuery();
                double price = 0;
                if (rsItem.next()) {
                    price = rsItem.getDouble("price");
                }
                psDetail.setDouble(4, price);
                psDetail.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            throw new ServletException(e);
        }
        
        // Clean session data
        session.removeAttribute("cart");
        session.removeAttribute("total");
        session.removeAttribute("shipping");
        
        // Response using bootstrap for a better appearence
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Confirm Purchase</title>");
        
        //Bootstrap link
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        // More bootstrap
        out.println("<div class='container mt-5'>");
        out.println("<h2 class='text-success'>Thanks for buy here!</h2>");
        out.println("<div class='alert alert-success'>");
        out.println("<p>Your tracking number is: <strong>" + trackingNumber + "</strong></p>");
        out.println("</div>");
        out.println("<a href='shop' class='btn btn-primary'>Go back to the shop</a>");
        out.println("</div>");

        out.println("</body></html>");
    }
}
