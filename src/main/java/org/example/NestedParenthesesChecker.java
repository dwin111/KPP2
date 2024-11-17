package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NestedParenthesesChecker {

    public static void main(String[] args) {
        // Отримуємо шлях до поточної директорії
        String currentDirectory = Paths.get("").toAbsolutePath().toString();
        String filePath = currentDirectory + File.separator + "textInput.txt";

        // Перевіряємо, чи існує файл, якщо ні — створюємо
        ensureFileExists(filePath);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Оберіть спосіб введення тексту:");
        System.out.println("1. Зчитати текст із файлу (" + filePath + ")");
        System.out.println("2. Ввести текст вручну");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Читаємо новий рядок після вводу числа

        String text = "";

        if (choice == 1) {
            try {
                text = readFile(filePath);
                System.out.println("Текст зчитано із файлу.");
            } catch (IOException e) {
                System.out.println("Помилка читання файлу: " + e.getMessage());
                return;
            }
        } else if (choice == 2) {
            System.out.println("Введіть текст:");
            text = scanner.nextLine();
        } else {
            System.out.println("Невірний вибір!");
            return;
        }

        // Розділяємо текст на речення
        String[] sentences = text.split("\\.\\s*");
        Pattern nestedParenthesesPattern = Pattern.compile("\\(([^()]*(\\([^()]*\\)[^()]*)+)\\)");

        System.out.println("Речення з вкладеними більше, ніж 1 рівень круглими дужками:");
        for (String sentence : sentences) {
            Matcher matcher = nestedParenthesesPattern.matcher(sentence);
            if (matcher.find()) {
                System.out.println(sentence.trim());
            }
        }
    }

    // Метод для читання тексту з файлу
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString();
    }

    // Метод для перевірки існування файлу та створення прикладного файлу, якщо його немає
    private static void ensureFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            String exampleText = """
                Вставте свій текст сюди. Наприклад: У цьому реченні є (одна пара дужок). А тут є (вкладені (дужки) всередині) тексту.
                """;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(exampleText);
                System.out.println("Файл створено: " + filePath);
                System.out.println("Ви можете відредагувати його та запустити програму знову.");
            } catch (IOException e) {
                System.out.println("Не вдалося створити файл: " + e.getMessage());
            }
        }
    }
}
