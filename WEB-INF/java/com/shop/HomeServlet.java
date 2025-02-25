package com.shop;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Welcome to Our Shop</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("</head>");
        out.println("<body class='d-flex justify-content-center align-items-center vh-100 bg-light'>");

        out.println("<div class='container text-center'>");
        out.println("<h1 class='mb-4'>Welcome to Our Shop</h1>");
        out.println("<div class='row justify-content-center'>");

        // Enter as Guest
        out.println("<div class='col-md-4 mb-3'>");
        out.println("<a href='login' class='btn btn-secondary btn-lg w-100'>Login</a>");
        out.println("</div>");

        // Register as Customer
        out.println("<div class='col-md-4 mb-3'>");
        out.println("<a href='register' class='btn btn-primary btn-lg w-100'>Register</a>");
        out.println("</div>");

        // I'm a Customer (Login)
        out.println("<div class='col-md-4 mb-3'>");
        out.println("<a href='sellerLogin' class='btn btn-success btn-lg w-100'>I'm a Seller</a>");
        out.println("</div>");

        out.println("</div>");
        out.println("</div>");

        out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}
