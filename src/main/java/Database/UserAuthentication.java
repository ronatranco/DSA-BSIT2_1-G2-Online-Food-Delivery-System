package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthentication {

   
    private static final String SQL =
        "SELECT 1 FROM accounts "
      + "WHERE (username = ? OR email = ?) "
      + "AND password_hash = SHA2(?, 256) AND role = ?";

    public boolean authenticate(String login, String password, String role) throws SQLException {

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setString(1, login);
            ps.setString(2, login);
            ps.setString(3, password);         
            ps.setString(4, role.toUpperCase()); 

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }
}