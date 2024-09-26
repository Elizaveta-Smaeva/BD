package institut;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

//1. Графический интерфейс:
//   - Используется `JFrame` для создания основного окна.
//   - `JPanel` с `GridBagLayout` для размещения компонентов.
//   - Поля ввода для `id`, `surname`, `name` и `age`.
//   - Кнопка "Отправить" для вставки данных.

//2. Валидация данных:
//   - Проверяется, чтобы все поля были заполнены.
//   - id и age должны быть положительными целыми числами.
//   - Обрабатываются ошибки формата ввода.

//3. Вставка данных:
//   - Используется PreparedStatement для безопасной вставки данных и предотвращения SQL-инъекций.

//4. Обработка исключений:
//   - Ошибки соединения с базой данных.
//   - Нарушение ограничений целостности данных (например, дублирование первичного ключа).
//   - Неправильный формат ввода данных пользователем.

//5. Запуск приложения:
//   - Метод main устанавливает внешний вид интерфейса в соответствии с системой и запускает форму.


public class StudentForm extends JFrame {

    // Поля формы
    private JTextField surnameField;
    private JTextField nameField;
    private JTextField ageField;
    private JButton submitButton;

    // Конфигурация базы данных
    private final String DB_URL = "jdbc:postgresql://localhost:5432/newDB";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "qwerty";
    public StudentForm() {
        setTitle("Форма ввода студента");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрирование окна

        // Инициализация компонентов
        initComponents();
    }

    private void initComponents() {
        // Панель для размещения компонентов
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel surnameLabel = new JLabel("Фамилия:");
        surnameField = new JTextField(20);

        JLabel nameLabel = new JLabel("Имя:");
        nameField = new JTextField(20);

        JLabel ageLabel = new JLabel("Возраст:");
        ageField = new JTextField(20);

        submitButton = new JButton("Отправить");

        // Размещение компонентов на панели
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Начинаем размещение с фамилии
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(surnameLabel, gbc);

        gbc.gridx = 1;
        panel.add(surnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);

        // Добавление панели в окно
        add(panel);

        // Добавление обработчика события для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        // Получение и валидация данных
        String surname = surnameField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();

        if (surname.isEmpty() || name.isEmpty() || ageText.isEmpty()) {
            showErrorDialog("Все поля обязательны для заполнения.");
            return;
        }

        // Валидация возраста
        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showErrorDialog("Возраст должен быть положительным числом.");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Возраст должен быть числом.");
            return;
        }

        // Валидация фамилии и имени
        if (!isValidRussianName(surname)) {
            showErrorDialog("Фамилия должна содержать только русские буквы, первая буква должна быть заглавной.");
            return;
        }

        if (!isValidRussianName(name)) {
            showErrorDialog("Имя должно содержать только русские буквы, первая буква должна быть заглавной.");
            return;
        }

        // Капитализация первой буквы
        String formattedSurname = capitalizeFirstLetter(surname);
        String formattedName = capitalizeFirstLetter(name);

        // Вставка данных в базу
        String insertSQL = "INSERT INTO student (surname, name, age) VALUES (?, ?, ?);";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, formattedSurname);
            pstmt.setString(2, formattedName);
            pstmt.setInt(3, age);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Студент успешно добавлен.");
                clearForm();
            } else {
                showErrorDialog("Ошибка при добавлении студента.");
            }

        } catch (SQLException e) {
            showErrorDialog("Ошибка при вставке данных: " + e.getMessage());
        }
    }

    private boolean isValidRussianName(String name) {
        // Регулярное выражение для проверки русских букв с заглавной первой буквой
        String regex = "^[А-ЯЁ][а-яё]+$";
        return Pattern.matches(regex, name);
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void clearForm() {
        surnameField.setText("");
        nameField.setText("");
        ageField.setText("");
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentForm().setVisible(true);
            }
        });
    }
}