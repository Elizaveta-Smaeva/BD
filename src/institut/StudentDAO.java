package institut;

import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    void createTableIfNotExists();
    void addStudent(Student student) throws SQLException;
    List<Student> getAllStudents() throws SQLException;
}