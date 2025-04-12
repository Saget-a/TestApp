package test.textboxes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/test-app";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    // Метод для проверки логина и пароля
    public static boolean checkLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true, если пользователь найден

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для добавления нового пользователя
    public static boolean addUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Здесь пароли в открытом виде

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Если хотя бы одна строка была добавлена

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}