package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBTest {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/schooldb?useSSL=false&serverTimezone=UTC";
            String user = "root"; // your DB user
            String password = ""; // your DB password

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected Succesfully");

            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        }
    }
}
