package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class databaseConnection {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String NAME = "grabpanda";
    private static final String USER = "root";
    private static final String PASS = "";

    private static final String OPTIONS = "?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String SERVER_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + OPTIONS;
    private static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME + OPTIONS;

    private static boolean initialized = false;

    private static synchronized void initialize() throws SQLException {
        if (initialized) return;

        try (Connection con = DriverManager.getConnection(SERVER_URL, USER, PASS);
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + NAME + "`");
        }

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement st = con.createStatement()) {

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS accounts ("
              + " id            INT AUTO_INCREMENT PRIMARY KEY,"
              + " full_name     VARCHAR(100) NOT NULL,"
              + " username      VARCHAR(50)  NOT NULL UNIQUE,"
              + " email         VARCHAR(100) NOT NULL UNIQUE,"
              + " phone         VARCHAR(11)  NULL,"
              + " address       VARCHAR(255) NULL,"
              + " password_hash CHAR(64)     NOT NULL,"
              + " role          VARCHAR(20)  NOT NULL,"
              + " status        VARCHAR(20)  NOT NULL DEFAULT 'Active')");

            try (ResultSet rs = con.getMetaData().getColumns(con.getCatalog(), null, "accounts", "status")) {
                if (!rs.next()) {
                    st.executeUpdate("ALTER TABLE accounts ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'Active'");
                }
            }

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS orders ("
              + " id              INT AUTO_INCREMENT PRIMARY KEY,"
              + " order_code      VARCHAR(30)  NOT NULL UNIQUE,"
              + " customer_id     INT          NULL,"
              + " restaurant_id   INT          NULL,"
              + " restaurant_name VARCHAR(100) NOT NULL,"
              + " location        VARCHAR(255) NULL,"
              + " payment_method  VARCHAR(30)  NULL,"
              + " urgency         VARCHAR(20)  NOT NULL DEFAULT 'Standard',"
              + " status          VARCHAR(30)  NOT NULL DEFAULT 'Pending',"
              + " total           DECIMAL(10,2) NOT NULL DEFAULT 0,"
              + " created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS order_items ("
              + " id                   INT AUTO_INCREMENT PRIMARY KEY,"
              + " order_id             INT          NOT NULL,"
              + " item_name            VARCHAR(100) NOT NULL,"
              + " quantity             INT          NOT NULL,"
              + " unit_price           DECIMAL(10,2) NOT NULL,"
              + " special_instructions VARCHAR(255) NULL,"
              + " FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE)");
        }

        initialized = true;
    }

    public static Connection getConnection() throws SQLException {
        initialize();
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}