package com.shop;
import java.sql.*;

public class DBUtil {
    public static Connection getConnection() throws Exception {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver"); 

        // Connect to the database
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/WEBSHOP?serverTimezone=UTC", "shop", "1234");
    }
}
