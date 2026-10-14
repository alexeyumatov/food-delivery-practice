package delivery.console;

import java.util.Scanner;

/**
 * Чтение данных с клавиатуры. При неправильном вводе переспрашивает.
 */
public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readNotEmpty(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Значение не может быть пустым.");
        }
    }

    /**
     * Читает целое число в диапазоне [min, max].
     */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Введите число от " + min + " до " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Это не целое число, попробуйте ещё раз.");
            }
        }
    }

    public int readPositiveInt(String prompt) {
        return readInt(prompt, 1, Integer.MAX_VALUE);
    }

    /**
     * Читает положительное дробное число. Можно писать и через точку, и через запятую.
     */
    public double readPositiveDouble(String prompt) {
        while (true) {
            String line = readLine(prompt).replace(',', '.');
            try {
                double value = Double.parseDouble(line);
                if (value > 0 && !Double.isInfinite(value)) {
                    return value;
                }
                System.out.println("Число должно быть больше нуля.");
            } catch (NumberFormatException e) {
                System.out.println("Это не число, попробуйте ещё раз.");
            }
        }
    }
}
