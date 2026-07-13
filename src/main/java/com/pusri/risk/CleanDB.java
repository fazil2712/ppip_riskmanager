package com.pusri.risk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class CleanDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://riskmanager-mysql.mysql.database.azure.com:3306/riskdb?useSSL=true&requireSSL=false";
        String user = "riskadmin";
        String pass = "RiskManager@123";
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connected to Azure MySQL!");
            Statement stmt = conn.createStatement();
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            while (rs.next()) {
                String table = rs.getString(1);
                System.out.println("Dropping table " + table);
                Statement dropStmt = conn.createStatement();
                dropStmt.execute("DROP TABLE `" + table + "`");
                dropStmt.close();
            }
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            System.out.println("All tables dropped successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
