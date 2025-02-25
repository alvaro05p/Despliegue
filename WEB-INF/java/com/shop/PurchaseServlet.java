package com.shop;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class PurchaseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, Integer> cart = new HashMap<>();
        double total = 0;
        int totalItems = 0;
        
        try (Connection conn = DBUtil.getConnection()) {
            // Loop the article list to get the selected items
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Items");
            while (rs.next()) {
                int itemId = rs.getInt("id");
                String param = request.getParameter("item_" + itemId);
                int quantity = 0;
                if (param != null) {
                    quantity = Integer.parseInt(param);
                }
                if (quantity > 0) {
                    cart.put(itemId, quantity);
                    totalItems += quantity;
                }
            }
            // Logic to calculate the total
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                PreparedStatement ps = conn.prepareStatement("SELECT price FROM Items WHERE id = ?");
                ps.setInt(1, entry.getKey());
                ResultSet rsItem = ps.executeQuery();
                if (rsItem.next()) {
                    double price = rsItem.getDouble("price");
                    total += price * entry.getValue();
                }
            }
        } catch(Exception e) {
            throw new ServletException(e);
        }
        
        // Shipping calc
        double shipping = 2 + totalItems * 1;
        session.setAttribute("cart", cart);
        session.setAttribute("total", total);
        session.setAttribute("shipping", shipping);
        
        // Buy overview
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Buy overview</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        // Contenedor principal
        out.println("<div class='container mt-5'>");
        out.println("<h2>Buy overview</h2>");
        
        // Tabla de los artículos
        out.println("<table class='table table-striped'><thead><tr><th>Article</th><th>Quantity</th><th>Unitary price</th></tr></thead><tbody>");
        
        try (Connection conn = DBUtil.getConnection()) {
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                PreparedStatement ps = conn.prepareStatement("SELECT name, price FROM Items WHERE id = ?");
                ps.setInt(1, entry.getKey());
                ResultSet rsItem = ps.executeQuery();
                if (rsItem.next()) {
                    String name = rsItem.getString("name");
                    double price = rsItem.getDouble("price");
                    out.println("<tr><td>" + name + "</td><td>" + entry.getValue() + "</td><td>" + price + "€</td></tr>");
                }
            }
        } catch(Exception e) {
            out.println("<tr><td colspan='3'>Error: " + e.getMessage() + "</td></tr>");
        }
        
        out.println("</tbody></table>");
        
        // Total summary
        out.println("<p><strong>Total: " + total + "€</strong></p>");
        out.println("<p>Shipping consts: " + shipping + "€</p>");
        out.println("<p><strong>Total + Shipping: " + (total + shipping) + "€</strong></p>");
        
        out.println("<a href='shop' class='btn btn-secondary'>Back to the shop</a><br/><br/>");
        
        // Button to confirm the purchase
        out.println("<form method='post' action='confirmPurchase'>");
        out.println("<button type='submit' class='btn btn-success'>Confirm purchase</button>");
        out.println("</form>");
        
        out.println("</div>");
        out.println("</body></html>");
    }
}
