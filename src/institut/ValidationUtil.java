package institut;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class ValidationUtil {

    // Шаблон для проверки русских имен (первая буква заглавная, остальные строчные)
    private static final Pattern RUSSIAN_NAME_PATTERN = Pattern.compile("^[А-ЯЁ][а-яё]+$");

    /**
     * Проверяет, что ни одно из переданных полей не пустое.
     */
    public static boolean areFieldsNotEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет, является ли строка положительным целым числом.
     */
    public static boolean isPositiveInteger(String text) {
        try {
            int value = Integer.parseInt(text);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Проверяет, соответствует ли имя русскому формату (только русские буквы, первая буква заглавная).
     */
    public static boolean isValidRussianName(String name) {
        return RUSSIAN_NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Отображает диалоговое окно с сообщением об ошибке.
     */
    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Отображает диалоговое окно с информационным сообщением.
     */
    public static void showInfoDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }
}
