package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private static String login;

    public static void setLogin(String value) { login = value; }
    public static String getLogin() { return login; }
    public static void clear() { login = null; }
    public static final String STATUS_ACTIVE  = "Active";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_BANNED  = "Banned";
    public enum RegisterResult { SUCCESS, DUPLICATE }
    
    public static class Account {
        public String fullName, username, email, phone, address;
    }
    public static RegisterResult register(String fullName, String username, String email,
                                          String phone, String address,
                                          String password, String role) throws SQLException {
        // restaurants must wait for admin approval, customers are active right away
        String status = "RESTAURANT".equalsIgnoreCase(role) ? STATUS_PENDING : STATUS_ACTIVE;

        String sql = "INSERT INTO accounts "
                   + "(full_name, username, email, phone, address, password_hash, role, status) "
                   + "VALUES (?, ?, ?, ?, ?, SHA2(?, 256), ?, ?)";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setString(6, password);
            ps.setString(7, role.toUpperCase());
            ps.setString(8, status);
            ps.executeUpdate();
            return RegisterResult.SUCCESS;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) return RegisterResult.DUPLICATE; // username/email taken
            throw ex;
        }
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

    public static class RestaurantInfo {
        public int id;
        public String name;
        public String area;
    }

    public static List<RestaurantInfo> listActiveRestaurants() throws SQLException {
        String sql = "SELECT id, full_name, address FROM accounts "
                   + "WHERE UPPER(role) = 'RESTAURANT' AND status = ? "
                   + "ORDER BY full_name";

        List<RestaurantInfo> list = new ArrayList<>();
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, STATUS_ACTIVE);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RestaurantInfo r = new RestaurantInfo();
                    r.id = rs.getInt("id");
                    r.name = rs.getString("full_name");
                    String address = rs.getString("address");
                    r.area = (address == null) ? "" : address.trim();
                    list.add(r);
                }
            }
        }
        return list;
    }
}
