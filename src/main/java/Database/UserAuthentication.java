package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthentication {

    public enum Result { SUCCESS, INVALID, PENDING, BANNED }

    private static final String SQL =
        "SELECT status FROM accounts "
      + "WHERE (username = ? OR email = ?) "
      + "AND password_hash = SHA2(?, 256) AND role = ?";

    public Result authenticate(String login, String password, String role) throws SQLException {

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setString(1, login);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.setString(4, role.toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Result.INVALID;   // wrong username/password/role

                String status = rs.getString("status");
                if (AccountDAO.STATUS_PENDING.equalsIgnoreCase(status)) return Result.PENDING;
                if (AccountDAO.STATUS_BANNED.equalsIgnoreCase(status))  return Result.BANNED;
                return Result.SUCCESS;
            }
        }
    }
}