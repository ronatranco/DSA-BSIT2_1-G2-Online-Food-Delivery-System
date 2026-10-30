package Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;



public class databaseConnection {
    
    private static final Properties props = new Properties();
    private static boolean initialized = false;

    // check if theres db.properties
    static {
        try (FileInputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("No db.properties found, using defaults.");
        }
    }

    private static String host() { return props.getProperty("db.host", "localhost"); }
    private static String port() { return props.getProperty("db.port", "3306"); }
    private static String name() { return props.getProperty("db.name", "grabpanda"); }
    private static String user() { return props.getProperty("db.user", "root"); }
    private static String pass() { return props.getProperty("db.password", ""); }

    private static final String OPTIONS = "?useSSL=false&allowPublicKeyRetrieval=true";

    /** Creates the database and tables if  does not exist. */
    private static synchronized void initialize() throws SQLException {
        if (initialized) return;


        String serverUrl = "jdbc:mysql://" + host() + ":" + port() + "/" + OPTIONS;
        try (Connection con = DriverManager.getConnection(serverUrl, user(), pass());
             Statement st = con.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + name() + "`");
        }

        // creating the tables
        try (Connection con = DriverManager.getConnection(dbUrl(), user(), pass());
             Statement st = con.createStatement()) {
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users ("
              + " id INT AUTO_INCREMENT PRIMARY KEY,"
              + " full_name VARCHAR(100) NOT NULL,"
              + " username  VARCHAR(50)  NOT NULL UNIQUE,"
              + " email     VARCHAR(100) NOT NULL UNIQUE,"
              + " phone     VARCHAR(11)  NOT NULL,"
              + " address   VARCHAR(255) NOT NULL,"
              + " password  VARCHAR(255) NOT NULL,"
              + " role      VARCHAR(20)  NOT NULL)");

            // Add more CREATE TABLE IF NOT EXISTS statements here
            // (menu items, orders, etc.)
        }
        initialized = true;
    }

    private static String dbUrl() {
        return "jdbc:mysql://" + host() + ":" + port() + "/" + name() + OPTIONS;
    }

    public static Connection getConnection() throws SQLException {
        initialize();
        return DriverManager.getConnection(dbUrl(), user(), pass());
    }
}