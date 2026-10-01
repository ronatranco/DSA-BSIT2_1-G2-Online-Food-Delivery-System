package gui;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseTest {

    public static void main(String[] args) {

        String sql =
                "INSERT INTO user_auth "
                + "(full_name, email_address, username, "
                + "phone_number, address, password) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(1, "Java Test");
            statement.setString(2, "javatest@gmail.com");
            statement.setString(3, "javatest");
            statement.setString(4, "09123456789");
            statement.setString(5, "Test Address");
            statement.setString(6, "test123");

            int rows = statement.executeUpdate();

            System.out.println("Rows inserted: " + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
