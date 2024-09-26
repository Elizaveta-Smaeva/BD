package institut;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                + "id SERIAL PRIMARY KEY, "
                + "surname VARCHAR(50) NOT NULL, "
                + "name VARCHAR(50) NOT NULL, "
                + "age INTEGER NOT NULL"
                + ");";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Таблица 'student' готова.");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void addStudent(Student student) throws SQLException {
        String insertSQL = "INSERT INTO student (surname, name, age) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, student.getSurname());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getAge());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        String selectSQL = "SELECT id, surname, name, age FROM student";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("surname"),
                        rs.getString("name"),
                        rs.getInt("age")
                );
                students.add(student);
            }
        }
        return students;
    }
}
