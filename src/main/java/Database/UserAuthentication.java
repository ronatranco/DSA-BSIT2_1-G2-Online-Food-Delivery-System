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

public class UserAuthentication {

    private String sql_query;
    private PreparedStatement ps;
    
    /** Returns true if login (username or email) + password match a user with this role. */
    public boolean authenticate(String login, String password, String role) throws SQLException {
        String sql_query = "SELECT password FROM users "
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
}