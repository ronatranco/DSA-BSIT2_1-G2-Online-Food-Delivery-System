package Database;

import java.sql.Connection;
import java.sql.DriverManager;
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
              + " role          VARCHAR(20)  NOT NULL)");

            st.executeUpdate(
                "INSERT IGNORE INTO accounts "
              + "(full_name, username, email, password_hash, address, role) VALUES ("
              + "'System Administrator', 'admin', 'admin@grabPanda.local', "
              + "SHA2('Admin@123', 256), 'PUP Binan, Laguna,', 'ADMIN')" );
            st.executeUpdate(
                "INSERT IGNORE INTO accounts "
              + "(full_name, username, email, password_hash, address, role) VALUES ("
              + "'McDollibee', 'mcdollibee', 'mcdollibee@grabPanda.local', "
              + "SHA2('mcdollibee123', 256), 'PUP Binan, Laguna,', 'RESTAURANT')" );
            st.executeUpdate(
                "INSERT IGNORE INTO accounts "
              + "(full_name, username, email, password_hash, address, role) VALUES ("
              + "'Brain cells Milk Tea', 'braincellmilktea', 'braincellmilktea@grabPanda.local', "
              + "SHA2('braincellmilktea123', 256), 'PUP Binan, Laguna,', 'RESTAURANT')" );
            st.executeUpdate(
                "INSERT IGNORE INTO accounts "
              + "(full_name, username, email, password_hash, address, role) VALUES ("
              + "'Mang Pinaasa', 'mangpinaasa', 'mangpinaasa@grabPanda.local', "
              + "SHA2('mangpinaasa123', 256), 'PUP Binan, Laguna,', 'RESTAURANT')" );
        }
        initialized = true;
    }

    public static Connection getConnection() throws SQLException {
        initialize();
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}