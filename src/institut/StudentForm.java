package institut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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
        // Получение данных из полей
        String surname = surnameField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();

        // Валидация полей на непустоту
        if (!ValidationUtil.areFieldsNotEmpty(surname, name, ageText)) {
            ValidationUtil.showErrorDialog(this, "Все поля обязательны для заполнения.");
            return;
        }

        // Валидация возраста
        if (!ValidationUtil.isPositiveInteger(ageText)) {
            ValidationUtil.showErrorDialog(this, "Возраст должен быть положительным числом.");
            return;
        }

        int age = Integer.parseInt(ageText);

        // Валидация фамилии и имени
        if (!ValidationUtil.isValidRussianName(surname)) {
            ValidationUtil.showErrorDialog(this, "Фамилия должна содержать только русские буквы, первая буква должна быть заглавной.");
            return;
        }

        if (!ValidationUtil.isValidRussianName(name)) {
            ValidationUtil.showErrorDialog(this, "Имя должно содержать только русские буквы, первая буква должна быть заглавной.");
            return;
        }

        Student student = new Student(surname, name, age);

        try {
            studentDAO.addStudent(student);
            ValidationUtil.showInfoDialog(this, "Студент успешно добавлен.");
            clearForm();
        } catch (SQLException ex) {
            ValidationUtil.showErrorDialog(this, "Ошибка при добавлении студента: " + ex.getMessage());
        }
    }

    /**
     * Очищает поля формы после успешного добавления студента.
     */
    private void clearForm() {
        surnameField.setText("");
        nameField.setText("");
        ageField.setText("");
    }

    // Основной метод для запуска формы
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentForm form = new StudentForm();
            form.setVisible(true);
        });
    }
}
