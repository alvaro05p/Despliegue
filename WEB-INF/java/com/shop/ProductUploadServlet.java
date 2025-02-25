package com.shop;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ProductUploadServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<title>Upload Product</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");
        
        out.println("<div class='container mt-5'>");
        out.println("<h2>Upload New Product</h2>");
        out.println("<form action='uploadProduct' method='post'>");
        out.println("<div class='mb-3'>");
        out.println("<label for='productName' class='form-label'>Product Name:</label>");
        out.println("<input type='text' class='form-control' id='productName' name='productName' required>");
        out.println("</div>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='price' class='form-label'>Price:</label>");
        out.println("<input type='number' class='form-control' id='price' name='price' step='0.01' required>");
        out.println("</div>");
        
        out.println("<div class='mb-3'>");
        out.println("<label for='description' class='form-label'>Description:</label>");
        out.println("<textarea class='form-control' id='description' name='description'></textarea>");
        out.println("</div>");
        
        out.println("<button type='submit' class='btn btn-primary'>Upload Product</button>");
        out.println("</form>");
        
        out.println("<a href='sellerDashboard' class='btn btn-secondary mt-3'>Back to Dashboard</a>");
        out.println("</div>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<title>Upload Product</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head><body>");

        // Get parameters from form
        String productName = request.getParameter("productName");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");

        // Price validation
        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            out.println("<h3 class='text-danger'>Invalid price format.</h3>");
            out.println("<a href='uploadProduct' class='btn btn-secondary mt-3'>Go Back</a>");
            out.println("</body></html>");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            String query = "INSERT INTO Items (name, price, description) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, productName);
                stmt.setDouble(2, price);
                stmt.setString(3, description);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("<div class='container mt-5'>");
                    out.println("<h3 class='text-success'>Product uploaded successfully!</h3>");
                    out.println("<a href='sellerDashboard' class='btn btn-primary'>Back to Dashboard</a>");
                    out.println("</div>");
                } else {
                    out.println("<div class='container mt-5'>");
                    out.println("<h3 class='text-danger'>Failed to upload product.</h3>");
                    out.println("<a href='uploadProduct' class='btn btn-secondary mt-3'>Go Back</a>");
                    out.println("</div>");
                }
            }
        } catch (Exception e) {
            out.println("<h3 class='text-danger'>Error: " + e.getMessage() + "</h3>");
            out.println("<a href='uploadProduct' class='btn btn-secondary mt-3'>Go Back</a>");
        }

        out.println("</body></html>");
    }
}
