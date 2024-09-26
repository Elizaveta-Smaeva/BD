package institut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class connection {
    // Параметры подключения к базе данных
    private static final String URL = "jdbc:postgresql://localhost:5432/newDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "qwerty";

    public static void main(String[] args) {
        // SQL-запрос для создания таблицы
        String createTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                + " id SERIAL PRIMARY KEY,"
                + "surname VARCHAR(50) NOT NULL, "
                + "name VARCHAR(50) NOT NULL, "
                + "age INTEGER NOT NULL"
                + ");";

        // Попытка установить соединение и выполнить запрос
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Выполнение SQL-запроса
            stmt.executeUpdate(createTableSQL);
            System.out.println("Таблица 'студент' успешно создана или уже существует.");

        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных или выполнении запроса.");
            e.printStackTrace();
        }
    }
}
