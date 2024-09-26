package institut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class StudentForm extends JFrame {

    // Поля формы
    private JTextField surnameField;
    private JTextField nameField;
    private JTextField ageField;
    private JButton submitButton;

    private StudentDAO studentDAO;

    public StudentForm() {
        studentDAO = new StudentDAOImpl();

        setTitle("Форма ввода студента");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрирование окна

        // Инициализация компонентов
        initComponents();

        // Создание таблицы, если не существует
        studentDAO.createTableIfNotExists();
    }

    private void initComponents() {
        // Панель для размещения компонентов
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Метки и поля ввода
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
        submitButton.addActionListener(new SubmitButtonListener());
    }

    // Внутренний класс для обработки события нажатия кнопки
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            submitForm();
        }
    }

    private void submitForm() {
        // Получение и валидация данных
        String surname = surnameField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        if (surname.isEmpty() | name.isEmpty() | ageText.isEmpty()) {
            showErrorDialog("Все поля обязательны для заполнения.");
            return;
        }
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

        Student student = new Student(surname, name, age);

        try {
            studentDAO.addStudent(student);
            showInfoDialog("Студент успешно добавлен.");
            clearForm();
        } catch (SQLException ex) {
            showErrorDialog("Ошибка при добавлении студента: " + ex.getMessage());
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
        JOptionPane.showMessageDialog(this,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Информация",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            StudentForm form = new StudentForm();
            form.setVisible(true);
        });
    }
}