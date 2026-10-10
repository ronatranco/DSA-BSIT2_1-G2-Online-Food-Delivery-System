package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

// Right-click this file in NetBeans -> Run File
public class TestConnection {
    public static void main(String[] args) {
        try (Connection con = databaseConnection.getConnection();
             Statement st = con.createStatement()) {

             st.executeUpdate("ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'customer'");
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
        }
    }
}