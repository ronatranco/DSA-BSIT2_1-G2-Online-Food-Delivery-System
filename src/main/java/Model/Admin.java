/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to edit this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.databaseConnection;
import Model.Restaurant.MoneyFormat;
import java.sql.*;
import java.util.*;

/**
 * @author rownavanana and calalang
 */

public class Admin {
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_RESTAURANT = "RESTAURANT";
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_BANNED = "Banned";
    public static final String STATUS_PENDING = "Pending";

    public static final String[] ZONES = {"Zone A", "Zone B", "Zone C"};

    public static final class Account {
        public int id;
        public String fullName, username, email, phone, address, status;
    }

    private static final String[] WANTED = {"id", "full_name", "username", "email", "phone", "address", "status"};

    public List<Account> listAccounts(String role) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try (Connection con = databaseConnection.getConnection()) {
            String table = accountsTable(con);
            Set<String> columns = columnsOf(con, table);

            StringBuilder select = new StringBuilder();
            for (String column : WANTED) {
                if (columns.contains(column)) {
                    select.append(select.length() == 0 ? "" : ", ").append(column);
                }
            }
            String sql = "SELECT " + select + " FROM " + table + " WHERE UPPER(role) = ? ORDER BY id";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, role.toUpperCase());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Account a = new Account();
                        a.id = rs.getInt("id");
                        a.fullName = text(rs, columns, "full_name");
                        a.username = text(rs, columns, "username");
                        a.email = text(rs, columns, "email");
                        a.phone = text(rs, columns, "phone");
                        a.address = text(rs, columns, "address");
                        String status = text(rs, columns, "status");
                        a.status = status.trim().isEmpty() ? STATUS_ACTIVE : status;
                        accounts.add(a);
                    }
                }
            }
        }
        return accounts;
    }

    public int countAccounts(String role) throws SQLException {
        try (Connection con = databaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM " + accountsTable(con) + " WHERE UPPER(role) = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, role.toUpperCase());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getInt(1) : 0;
                }
            }
        }
    }

    public static void ensureDistinctRestaurants() {
        String[] defaults = {"McDollibee", "Brain Cells Milk Tea", "Mang Pinaasa"};
        Restaurant.Brand[] brands = Restaurant.Brand.values();
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < brands.length; i++) {
            Restaurant data = Restaurant.get(brands[i]);
            Restaurant.Restaurantt r = data.restaurant;
            boolean changed = false;
            if (!seen.add(r.name.trim().toLowerCase())) {
                r.name = defaults[i % defaults.length];
                seen.add(r.name.toLowerCase());
                changed = true;
            }
            if (!Arrays.asList(ZONES).contains(r.location)) {
                r.location = ZONES[i % ZONES.length];
                changed = true;
            }
            if (changed) {
                data.notifyChanged();
            }
        }
    }

    public static final String ROLE_ADMIN = "ADMIN";

    public static final class Profile {
        public int id = -1;
        public String fullName = "Administrator", username = "admin", email = "", phone = "", address = "";

        Profile copy() {
            Profile p = new Profile();
            p.id = id;
            p.fullName = fullName;
            p.username = username;
            p.email = email;
            p.phone = phone;
            p.address = address;
            return p;
        }
    }

    private static final Profile profile = new Profile();

    public Profile loadProfile() {
        try {
            List<Account> found = listAccounts(ROLE_ADMIN);
            if (!found.isEmpty()) {
                Account a = found.get(0);
                synchronized (profile) {
                    profile.id = a.id;
                    profile.username = a.username.isEmpty() ? profile.username : a.username;
                    profile.fullName = a.fullName.isEmpty() ? profile.fullName : a.fullName;
                    profile.email = a.email;
                    profile.phone = a.phone;
                    profile.address = a.address;
                }
            }
        } catch (SQLException ex) {
        }
        synchronized (profile) {
            return profile.copy();
        }
    }

    public void saveProfile(Profile p) throws SQLException {
        if (p.id >= 0) {
            updateAccount(p.id, p.fullName, p.email, p.phone, p.address);
        }
        synchronized (profile) {
            profile.fullName = p.fullName;
            profile.email = p.email;
            profile.phone = p.phone;
            profile.address = p.address;
        }
    }

    public void addAccount(String role, String fullName, String username, String email,
                           String phone, String address, String password) throws SQLException {
        try (Connection con = databaseConnection.getConnection()) {
            String table = accountsTable(con);
            if (!columnsOf(con, table).contains("password_hash")) {
                throw new SQLException("Table '" + table + "' has no password_hash column, so accounts can't be created here.");
            }
            String sql = "INSERT INTO " + table
                    + " (full_name, username, email, phone, address, password_hash, role) VALUES (?, ?, ?, ?, ?, SHA2(?, 256), ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, fullName);
                ps.setString(2, username);
                ps.setString(3, email);
                ps.setString(4, phone);
                ps.setString(5, address);
                ps.setString(6, password);
                ps.setString(7, role.toUpperCase());
                ps.executeUpdate();
            }
        }
    }

    public void updateAccount(int id, String fullName, String email, String phone, String address) throws SQLException {
        try (Connection con = databaseConnection.getConnection()) {
            String sql = "UPDATE " + accountsTable(con) + " SET full_name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, address);
                ps.setInt(5, id);
                ps.executeUpdate();
            }
        }
    }

    public void setStatus(int id, String status) throws SQLException {
        try (Connection con = databaseConnection.getConnection()) {
            String table = accountsTable(con);
            if (!columnsOf(con, table).contains("status")) {
                throw new SQLException("Table '" + table + "' has no 'status' column. Add one with: ALTER TABLE "
                        + table + " ADD COLUMN status VARCHAR(20) DEFAULT 'Active';");
            }
            try (PreparedStatement ps = con.prepareStatement("UPDATE " + table + " SET status = ? WHERE id = ?")) {
                ps.setString(1, status);
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        }
    }

    public void deleteAccount(int id) throws SQLException {
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM " + accountsTable(con) + " WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private static volatile String resolvedTable;

    private static String accountsTable(Connection con) throws SQLException {
        String table = resolvedTable;
        if (table == null) {
            table = tableExists(con, "accounts") ? "accounts" : "users";
            resolvedTable = table;
        }
        return table;
    }

    private static boolean tableExists(Connection con, String name) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();
        try (ResultSet rs = meta.getTables(con.getCatalog(), null, name, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private static Set<String> columnsOf(Connection con, String table) throws SQLException {
        Set<String> columns = new HashSet<>();
        try (ResultSet rs = con.getMetaData().getColumns(con.getCatalog(), null, table, null)) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
        }
        return columns;
    }

    private static String text(ResultSet rs, Set<String> columns, String column) throws SQLException {
        if (!columns.contains(column)) {
            return "";
        }
        String value = rs.getString(column);
        return value == null ? "" : value;
    }

    public static final String FAILED = "Failed";

    public static final class Report {
        public final String title;
        public final String[] columns;
        public final List<Object[]> rows = new ArrayList<>();
        public String summary = "";

        Report(String title, String... columns) {
            this.title = title;
            this.columns = columns;
        }
    }

    public static Report salesReport() {
        Report report = new Report("Sales Report",
                "Restaurant", "Orders", "Delivered", "Delivered Revenue", "Avg. Order Value", "Items Sold", "Best Seller");
        int orders = 0, delivered = 0;
        double revenue = 0;
        long itemsSold = 0;

        for (Restaurant.Brand brand : Restaurant.Brand.values()) {
            Restaurant data = Restaurant.get(brand);
            int rDelivered = 0;
            double rRevenue = 0;
            for (Restaurant.Order order : data.orders) {
                if (Restaurant.Order.COMPLETED.equals(order.status)) {
                    rDelivered++;
                    rRevenue += order.total;
                }
            }
            long rSold = 0;
            Restaurant.Dish best = null;
            for (Restaurant.Dish dish : data.restaurant.menuItems) {
                rSold += dish.sold;
                if (best == null || dish.sold > best.sold) {
                    best = dish;
                }
            }
            double average = rDelivered == 0 ? 0 : rRevenue / rDelivered;
            report.rows.add(new Object[]{data.restaurant.name, data.orders.size(), rDelivered,
                MoneyFormat.peso(rRevenue), MoneyFormat.peso(average), rSold,
                best == null || best.sold == 0 ? "-" : best.name});
            orders += data.orders.size();
            delivered += rDelivered;
            revenue += rRevenue;
            itemsSold += rSold;
        }
        report.rows.add(new Object[]{"ALL RESTAURANTS", orders, delivered, MoneyFormat.peso(revenue),
            MoneyFormat.peso(delivered == 0 ? 0 : revenue / delivered), itemsSold, ""});
        report.summary = "Revenue counts delivered orders only.";
        return report;
    }

    public static Report orderHistoryReport() {
        Report report = new Report("Order History",
                "Restaurant", "Order ID", "Customer", "Items", "Total", "Payment", "Time", "Status");
        for (Restaurant.Brand brand : Restaurant.Brand.values()) {
            Restaurant data = Restaurant.get(brand);
            for (Restaurant.Order o : data.orders) {
                report.rows.add(new Object[]{data.restaurant.name, o.id, o.customer, o.items,
                    MoneyFormat.peso(o.total), o.payment, o.time, o.status});
            }
        }
        report.summary = report.rows.size() + " order(s) across all restaurants.";
        return report;
    }

    public static Report deliveryPerformanceReport() {
        Report report = new Report("Delivery Performance",
                "Restaurant", "Total Orders", "In Progress", "Out for Delivery", "Delivered", "Cancelled", "Failed", "Success Rate");
        int total = 0, progress = 0, out = 0, delivered = 0, cancelled = 0, failed = 0;

        for (Restaurant.Brand brand : Restaurant.Brand.values()) {
            Restaurant data = Restaurant.get(brand);
            int rProgress = 0, rOut = 0, rDelivered = 0, rCancelled = 0, rFailed = 0;
            for (Restaurant.Order o : data.orders) {
                if (Restaurant.Order.COMPLETED.equals(o.status)) {
                    rDelivered++;
                } else if (Restaurant.Order.READY.equals(o.status)) {
                    rOut++;
                } else if (Restaurant.Order.CANCELLED.equals(o.status)) {
                    rCancelled++;
                } else if (FAILED.equals(o.status)) {
                    rFailed++;
                } else {
                    rProgress++;
                }
            }
            report.rows.add(new Object[]{data.restaurant.name, data.orders.size(), rProgress, rOut,
                rDelivered, rCancelled, rFailed, rate(rDelivered, rCancelled, rFailed)});
            total += data.orders.size();
            progress += rProgress;
            out += rOut;
            delivered += rDelivered;
            cancelled += rCancelled;
            failed += rFailed;
        }
        report.rows.add(new Object[]{"ALL RESTAURANTS", total, progress, out, delivered, cancelled, failed,
            rate(delivered, cancelled, failed)});
        report.summary = "Success rate = delivered \u00F7 (delivered + cancelled + failed). Orders still in progress are not counted.";
        return report;
    }

    private static String rate(int delivered, int cancelled, int failed) {
        int closed = delivered + cancelled + failed;
        return closed == 0 ? "n/a" : String.format("%.0f%%", 100.0 * delivered / closed);
    }

    public static class DeliveryGraph {
        public static final double AVG_SPEED_KMH = 25.0;

        public static final class Road {
            public final String a, b;
            public final double km;

            Road(String a, String b, double km) {
                this.a = a;
                this.b = b;
                this.km = km;
            }
        }

        public static final class Route {
            public final List<String> path;
            public final double km;

            Route(List<String> path, double km) {
                this.path = path;
                this.km = km;
            }

            public int estimatedMinutes() {
                return (int) Math.ceil(km / AVG_SPEED_KMH * 60.0);
            }

            @Override
            public String toString() {
                return String.join("  \u2192  ", path);
            }
        }

        private final Map<String, Map<String, Double>> adjacency = new TreeMap<>();
        private final Set<String> hubs = new TreeSet<>();

        public boolean addLocation(String name, boolean hub) {
            if (name == null || name.trim().isEmpty() || adjacency.containsKey(name)) {
                return false;
            }
            adjacency.put(name, new TreeMap<>());
            if (hub) {
                hubs.add(name);
            }
            return true;
        }

        public boolean hasLocation(String name) {
            return adjacency.containsKey(name);
        }

        public boolean isHub(String name) {
            return hubs.contains(name);
        }

        public boolean removeLocation(String name) {
            Map<String, Double> neighbours = adjacency.remove(name);
            if (neighbours == null) {
                return false;
            }
            for (String other : neighbours.keySet()) {
                adjacency.get(other).remove(name);
            }
            hubs.remove(name);
            return true;
        }

        public boolean renameLocation(String oldName, String newName) {
            if (!adjacency.containsKey(oldName) || newName == null || newName.trim().isEmpty()
                    || adjacency.containsKey(newName)) {
                return false;
            }
            Map<String, Double> neighbours = adjacency.remove(oldName);
            adjacency.put(newName, neighbours);
            for (String other : neighbours.keySet()) {
                Map<String, Double> back = adjacency.get(other);
                back.put(newName, back.remove(oldName));
            }
            if (hubs.remove(oldName)) {
                hubs.add(newName);
            }
            return true;
        }

        public List<String> locations() {
            return new ArrayList<>(adjacency.keySet());
        }

        public List<String> hubs() {
            return new ArrayList<>(hubs);
        }

        public boolean addRoad(String a, String b, double km) {
            if (a == null || b == null || a.equals(b) || km <= 0
                    || !adjacency.containsKey(a) || !adjacency.containsKey(b)) {
                return false;
            }
            adjacency.get(a).put(b, km);
            adjacency.get(b).put(a, km);
            return true;
        }

        public boolean removeRoad(String a, String b) {
            if (!adjacency.containsKey(a) || !adjacency.containsKey(b) || adjacency.get(a).remove(b) == null) {
                return false;
            }
            adjacency.get(b).remove(a);
            return true;
        }

        public List<Road> roads() {
            List<Road> result = new ArrayList<>();
            for (Map.Entry<String, Map<String, Double>> from : adjacency.entrySet()) {
                for (Map.Entry<String, Double> to : from.getValue().entrySet()) {
                    if (from.getKey().compareTo(to.getKey()) < 0) {
                        result.add(new Road(from.getKey(), to.getKey(), to.getValue()));
                    }
                }
            }
            return result;
        }

        public int degree(String name) {
            Map<String, Double> neighbours = adjacency.get(name);
            return neighbours == null ? 0 : neighbours.size();
        }

        public Route shortestRoute(String from, String to) {
            if (!adjacency.containsKey(from) || !adjacency.containsKey(to)) {
                return null;
            }
            Map<String, Double> dist = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<Map.Entry<String, Double>> queue =
                    new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));
            Set<String> done = new HashSet<>();

            dist.put(from, 0.0);
            queue.add(new AbstractMap.SimpleEntry<>(from, 0.0));
            while (!queue.isEmpty()) {
                String current = queue.poll().getKey();
                if (!done.add(current)) {
                    continue;
                }
                if (current.equals(to)) {
                    break;
                }
                for (Map.Entry<String, Double> edge : adjacency.get(current).entrySet()) {
                    double candidate = dist.get(current) + edge.getValue();
                    if (candidate < dist.getOrDefault(edge.getKey(), Double.POSITIVE_INFINITY)) {
                        dist.put(edge.getKey(), candidate);
                        previous.put(edge.getKey(), current);
                        queue.add(new AbstractMap.SimpleEntry<>(edge.getKey(), candidate));
                    }
                }
            }
            if (!dist.containsKey(to)) {
                return null;
            }
            LinkedList<String> path = new LinkedList<>();
            for (String at = to; at != null; at = previous.get(at)) {
                path.addFirst(at);
            }
            return new Route(path, dist.get(to));
        }

        public Set<String> reachableFrom(String start) {
            Set<String> seen = new LinkedHashSet<>();
            if (!adjacency.containsKey(start)) {
                return seen;
            }
            Deque<String> queue = new ArrayDeque<>();
            queue.add(start);
            seen.add(start);
            while (!queue.isEmpty()) {
                for (String next : adjacency.get(queue.poll()).keySet()) {
                    if (seen.add(next)) {
                        queue.add(next);
                    }
                }
            }
            return seen;
        }

        public List<String> unreachableFromHubs() {
            Set<String> covered = new HashSet<>();
            for (String hub : hubs) {
                covered.addAll(reachableFrom(hub));
            }
            List<String> missing = new ArrayList<>();
            for (String location : adjacency.keySet()) {
                if (!covered.contains(location)) {
                    missing.add(location);
                }
            }
            return missing;
        }

        public Route nearestHubRoute(String destination) {
            Route best = null;
            for (String hub : hubs) {
                Route route = shortestRoute(hub, destination);
                if (route != null && (best == null || route.km < best.km)) {
                    best = route;
                }
            }
            return best;
        }

        public static DeliveryGraph createDefault(List<String> hubNames) {
            DeliveryGraph g = new DeliveryGraph();
            String[] zones = {"Zone A", "Zone B", "Zone C", "Zone D", "Zone E", "Zone F"};
            for (String zone : zones) {
                g.addLocation(zone, false);
            }
            double[] ring = {2.4, 1.8, 3.1, 2.2, 2.9, 1.5};
            for (int i = 0; i < zones.length; i++) {
                g.addRoad(zones[i], zones[(i + 1) % zones.length], ring[i]);
            }
            g.addRoad("Zone A", "Zone D", 4.6);
            g.addRoad("Zone B", "Zone E", 5.2);

            for (int i = 0; i < hubNames.size(); i++) {
                String hub = hubNames.get(i);
                g.addLocation(hub, true);
                g.addRoad(hub, zones[(i * 2) % zones.length], 0.8 + 0.3 * i);
                g.addRoad(hub, zones[(i * 2 + 1) % zones.length], 1.2 + 0.3 * i);
            }
            return g;
        }
    }
}