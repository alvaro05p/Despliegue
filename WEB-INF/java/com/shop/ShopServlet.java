package com.shop;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ShopServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Article List</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        // Principal container
        out.println("<div class='container mt-5'>");
        out.println("<h2>Article List</h2>");
        
        out.println("<form method='post' action='purchase'>");
        
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Items");
            
            // Bootstrap item list
            while (rs.next()) {
                int itemId = rs.getInt("id");
                String itemName = rs.getString("name");
                double price = rs.getDouble("price");
                out.println("<div class='mb-3'>");
                out.println("<label class='form-label'>" + itemName + " - " + price + "€</label>");
                out.println("<select class='form-select' name='item_" + itemId + "'>");
                for (int i = 0; i <= 10; i++) {
                    out.println("<option value='" + i + "'>" + i + "</option>");
                }
                out.println("</select>");
                out.println("</div>");
            }
        } catch (Exception e) {
            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
        }
        
        out.println("<button type='submit' class='btn btn-primary'>Buy</button>");
        out.println("</form>");
        
        out.println("<a href='purchaseHistory' class='btn btn-link mt-3'>See buy history</a>");

        out.println("<div class='text-center mt-4'>");
        out.println("<a href='home' class='btn btn-danger'>Close</a>");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body></html>");
    }
}
