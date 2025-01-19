package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FinanceApp {
    private Map<String, User> users;
    private User currentUser;

    public FinanceApp() {
        users = new HashMap<>();
        currentUser = null;
    }

    public String register(String username, String password) {
        if (users.containsKey(username)) {
            return "Пользователь уже существует!";
        }
        users.put(username, new User(username, password));
        saveData(); // Сохраняем изменения при регистрации
        return "Регистрация успешна!";
    }

    public String login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return "Вход выполнен успешно!";
        }
        return "Неверные логин или пароль!";
    }

    public String logout() {
        currentUser = null;
        return "Вы вышли из системы!";
    }

    public void run() {
        loadData(); // Загружаем данные при запуске
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentUser == null) {
                // Главное меню (без авторизации)
                System.out.println("\nГлавное меню:");
                System.out.println("1. Регистрация нового пользователя");
                System.out.println("2. Авторизация");
                System.out.println("3. Выход");

                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> {
                        System.out.print("Введите логин: ");
                        String username = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();
                        System.out.println(register(username, password));
                    }
                    case "2" -> {
                        System.out.print("Введите логин: ");
                        String username = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();
                        System.out.println(login(username, password));
                    }
                    case "3" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверный выбор, попробуйте снова.");
                }
            } else {
                // Меню для авторизованного пользователя
                System.out.println("\nМеню пользователя:");
                System.out.println("1. Добавить доход");
                System.out.println("2. Добавить расход");
                System.out.println("3. Добавить категорию");
                System.out.println("4. Просмотреть сводку");
                System.out.println("5. Сохранить данные и выйти из аккаунта");
                System.out.println("6. Выйти из приложения");

                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> {
                        try {
                            System.out.print("Введите сумму дохода: ");
                            double amount = Double.parseDouble(scanner.nextLine());
                            System.out.println(currentUser.getWallet().addIncome(amount));
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод суммы. Попробуйте снова.");
                        }
                    }
                    case "2" -> {
                        // Сначала спрашиваем категорию
                        System.out.print("Введите категорию: ");
                        String category = scanner.nextLine();

                        // Проверяем, существует ли категория
                        if (!currentUser.getWallet().hasCategory(category)) {
                            System.out.println("Категория не найдена! Пожалуйста, добавьте её сначала.");
                            break;
                        }

                        // Если категория найдена, запрашиваем сумму
                        try {
                            System.out.print("Введите сумму расхода: ");
                            double amount = Double.parseDouble(scanner.nextLine());
                            System.out.println(currentUser.getWallet().addExpense(amount, category));
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод суммы. Попробуйте снова.");
                        }
                    }

                    case "3" -> {
                        try {
                            System.out.print("Введите название категории: ");
                            String name = scanner.nextLine();
                            System.out.print("Введите лимит категории: ");
                            double limit = Double.parseDouble(scanner.nextLine());
                            System.out.println(currentUser.getWallet().addCategory(name, limit));
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод лимита. Попробуйте снова.");
                        }
                    }
                    case "4" -> currentUser.getWallet().showSummary();
                    case "5" -> {
                        saveData();
                        System.out.println(logout());
                    }
                    case "6" -> {
                        saveData();
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверный выбор, попробуйте снова.");
                }
            }
        }
    }

    // Сохранение данных в файл
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
            oos.writeObject(users);
            System.out.println("Данные успешно сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // Загрузка данных из файла
    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.ser"))) {
            users = (Map<String, User>) ois.readObject();
            System.out.println("Данные успешно загружены.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл данных не найден. Будет создан новый.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }
}