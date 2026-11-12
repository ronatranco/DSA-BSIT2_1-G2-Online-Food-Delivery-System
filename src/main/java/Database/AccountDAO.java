package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    private static String login;

    public static void setLogin(String value) { login = value; }
    public static String getLogin() { return login; }
    public static void clear() { login = null; }

    public static class Account {
        public String fullName, username, email, phone, address;
    }

    public static Account findByLogin(String login) throws SQLException {
        String sql = "SELECT full_name, username, email, phone, address "
                   + "FROM accounts WHERE username = ? OR email = ?";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Account a = new Account();
                a.fullName = rs.getString("full_name");
                a.username = rs.getString("username");
                a.email    = rs.getString("email");
                a.phone    = rs.getString("phone");
                a.address  = rs.getString("address");
                return a;
            }
        }
    }
}