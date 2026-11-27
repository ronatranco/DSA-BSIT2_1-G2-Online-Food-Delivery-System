package Database;

import Model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class OrderDAO {

    // Saves the order and all its items in one transaction (all saved or nothing saved).
    // Returns the order code, e.g. "#ORD-2026-0007", built from the database id so it
    // never repeats, even after the app restarts.
    public static String insertOrder(Order order, String restaurantName, String location,
                                     String paymentMethod, double total) throws SQLException {

        try (Connection con = databaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Integer customerId   = findCustomerId(con, AccountDAO.getLogin());
                Integer restaurantId = findRestaurantId(con, restaurantName);

                // order_code is UNIQUE, so insert with a throwaway code first,
                // then replace it with the real one once we know the generated id
                String tempCode = "TMP-" + System.nanoTime();
                int orderId;

                String sql = "INSERT INTO orders (order_code, customer_id, restaurant_id, restaurant_name, "
                           + "location, payment_method, urgency, status, total) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending', ?)";
                try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, tempCode);
                    if (customerId == null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, customerId);
                    if (restaurantId == null) ps.setNull(3, java.sql.Types.INTEGER); else ps.setInt(3, restaurantId);
                    ps.setString(4, restaurantName);
                    ps.setString(5, location);
                    ps.setString(6, paymentMethod);
                    ps.setString(7, order.getUrgency());
                    ps.setDouble(8, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getInt(1);
                    }
                }

                String code = String.format("#ORD-%d-%04d", LocalDate.now().getYear(), orderId);
                try (PreparedStatement ps = con.prepareStatement("UPDATE orders SET order_code = ? WHERE id = ?")) {
                    ps.setString(1, code);
                    ps.setInt(2, orderId);
                    ps.executeUpdate();
                }

                String itemSql = "INSERT INTO order_items (order_id, item_name, quantity, unit_price, special_instructions) "
                               + "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                    for (Order.OrderItem item : order.getOrderItems()) {
                        ps.setInt(1, orderId);
                        ps.setString(2, item.getItemName());
                        ps.setInt(3, item.getQuantity());
                        ps.setDouble(4, item.getUnitPrice());
                        ps.setString(5, item.getSpecialInstructions());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();
                return code;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm a");

    // changes an order's status in the database
    public static void updateStatus(String orderCode, String status) throws SQLException {
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE orders SET status = ? WHERE order_code = ?")) {
            ps.setString(1, status);
            ps.setString(2, orderCode);
            ps.executeUpdate();
        }
    }
        // marks an order Received, but only if it belongs to the logged-in customer
    // and is currently Delivered. Returns true if it was updated.
    public static boolean markReceived(String orderCode) throws SQLException {
        String login = AccountDAO.getLogin();
        if (login == null) return false;

        String sql = "UPDATE orders SET status = 'Received' "
                   + "WHERE order_code = ? AND status = 'Delivered' "
                   + "AND customer_id = (SELECT id FROM accounts WHERE username = ? OR email = ? LIMIT 1)";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orderCode);
            ps.setString(2, login);
            ps.setString(3, login);
            return ps.executeUpdate() > 0;
        }
    }

    // the logged-in customer's orders, newest first, with their items.
    // active = true  -> not yet Received or Cancelled (the Track screen)
    // active = false -> Received or Cancelled (the History screen, later)
    public static List<Order.OrderData> listForCustomer(boolean active) throws SQLException {
        List<Order.OrderData> result = new ArrayList<>();
        String login = AccountDAO.getLogin();
        if (login == null) return result;

        String sql = "SELECT id, order_code, restaurant_name, location, payment_method, status, total, created_at "
                   + "FROM orders "
                   + "WHERE customer_id = (SELECT id FROM accounts WHERE username = ? OR email = ? LIMIT 1) "
                   + "AND status " + (active ? "NOT IN" : "IN") + " ('Received', 'Cancelled') "
                   + "ORDER BY id DESC";

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psItems = con.prepareStatement(
                     "SELECT item_name, quantity, unit_price, special_instructions FROM order_items WHERE order_id = ?")) {

            ps.setString(1, login);
            ps.setString(2, login);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    List<Order.OrderItem> items = new ArrayList<>();
                    psItems.setInt(1, rs.getInt("id"));
                    try (ResultSet ri = psItems.executeQuery()) {
                        while (ri.next()) {
                            items.add(new Order.OrderItem(ri.getString("item_name"), ri.getInt("quantity"),
                                    ri.getDouble("unit_price"), ri.getString("special_instructions")));
                        }
                    }
                    result.add(new Order.OrderData(
                            rs.getString("restaurant_name"),
                            rs.getString("location"),
                            rs.getString("order_code"),
                            rs.getTimestamp("created_at").toLocalDateTime().format(FMT),
                            "—",
                            rs.getString("payment_method"),
                            rs.getString("status"),
                            String.format("₱%.2f", rs.getDouble("total")),
                            items,
                            Order.OrderStore.getSessionCustomerId()));
                }
            }
        }
        return result;
    }
    // the logged-in customer's accounts.id (null if not found)
    private static Integer findCustomerId(Connection con, String login) throws SQLException {
        if (login == null) return null;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT id FROM accounts WHERE username = ? OR email = ?")) {
            ps.setString(1, login);
            ps.setString(2, login);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }

    // the restaurant's accounts.id, matched by name (null if not found)
    private static Integer findRestaurantId(Connection con, String name) throws SQLException {
        if (name == null) return null;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT id FROM accounts WHERE UPPER(role) = 'RESTAURANT' AND full_name = ? LIMIT 1")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }
}