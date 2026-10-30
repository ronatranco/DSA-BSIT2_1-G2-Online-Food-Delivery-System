/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Gideon Calalang
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class UserAuthentication {

    private String sql_query;
    private PreparedStatement ps;
    private Properties props;
    
    /** checks the hardcoded .properties file before proceeding to sql. */
    public boolean authenticate(String login, String password, String role) throws SQLException {
        if (checkProperties(login, password, role)) {
            return true;
        }
        sql_query = "SELECT password FROM users "
                   + "WHERE (username = ? OR email = ?) AND role = ?";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql_query)) {

            ps.setString(1, login);
            ps.setString(2, login);
            ps.setString(3, role);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getString("password").equals(password);
            }
        }
    }
        private boolean checkProperties(String login, String password, String role) {
        Properties props = new Properties();

        try (FileInputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            // no db.properties, just go to the database
            return false;
        }

        int i = 1;
        while (props.getProperty("account" + i) != null) {
            String[] parts = props.getProperty("account" + i).split("\\|");

            // need all 4 parts before authenticating
            if (parts.length >= 4) {
                String username = parts[0].trim();
                String email = parts[1].trim();
                String pass = parts[2].trim();
                String accRole = parts[3].trim();

                boolean loginMatches = login.equals(username) || login.equals(email);

                if (loginMatches && password.equals(pass) && role.equals(accRole)) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }
}