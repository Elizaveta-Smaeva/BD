package institut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    // Конфигурация базы данных
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/newDB";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "qwerty";

    static {
        try {
            // Регистрация драйвера PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Не удалось загрузить драйвер PostgreSQL.");
            e.printStackTrace();
        }
    }

    // Метод для получения подключения
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}