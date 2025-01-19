package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

class Wallet implements Serializable {
    private double balance;
    private Map<String, BudgetCategory> categories;
    private List<Transaction> transactions;

    public Wallet() {
        this.balance = 0.0;
        this.categories = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public String addCategory(String name, double budgetLimit) {
        if (categories.containsKey(name)) {
            return "Категория уже существует!";
        }
        categories.put(name, new BudgetCategory(name, budgetLimit));
        return "Категория '" + name + "' добавлена с лимитом " + budgetLimit;
    }

    public void showSummary() {
        double totalIncome = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + totalExpenses);
        System.out.println("Баланс: " + balance);

        System.out.println("\nБюджет по категориям:");
        for (Map.Entry<String, BudgetCategory> entry : categories.entrySet()) {
            BudgetCategory category = entry.getValue();
            System.out.println("Категория: " + category.getName() +
                    ", Потрачено: " + category.getSpent() +
                    ", Лимит: " + category.getRemainingBudget());
        }
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equals("income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getType().equals("expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getExpensesByCategories(List<String> categoryNames) {
        return transactions.stream()
                .filter(t -> t.getType().equals("expense") && categoryNames.contains(t.getCategoryName()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public String addIncome(double amount) {
        if (amount <= 0) {
            return "Сумма дохода должна быть больше 0!";
        }
        balance += amount;
        transactions.add(new Transaction("income", amount, null));
        return "Доход в размере " + amount + " добавлен. Баланс: " + balance;
    }

    public String addExpense(double amount, String categoryName) {
        if (amount <= 0) {
            return "Сумма расхода должна быть больше 0!";
        }
        if (amount > balance) {
            return "Недостаточно средств!";
        }
        BudgetCategory category = categories.get(categoryName);
        if (category == null) {
            String availableCategories = categories.keySet().isEmpty() ? "нет доступных категорий" : String.join(", ", categories.keySet());
            return "Категория не найдена! Доступные категории: " + availableCategories;
        }
        if (category.getRemainingBudget() < amount) {
            return "Превышен лимит категории!";
        }
        balance -= amount;
        category.addExpense(amount);
        transactions.add(new Transaction("expense", amount, categoryName));
        return "Расход в размере " + amount + " добавлен в категорию '" + categoryName + "'. Баланс: " + balance;
    }

    public void saveSummaryToFile (String filename){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Баланс: " + balance);
            for (Map.Entry<String, BudgetCategory> entry : categories.entrySet()) {
                BudgetCategory category = entry.getValue();
                writer.println("Категория: " + category.getName() +
                        ", Потрачено: " + category.getSpent() +
                        ", Осталось: " + category.getRemainingBudget());
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    public String transfer (User recipient,double amount){
        if (amount <= 0) {
            return "Сумма перевода должна быть больше 0!";
        }
        if (amount > balance) {
            return "Недостаточно средств для перевода!";
        }
        balance -= amount;
        recipient.getWallet().addIncome(amount);
        transactions.add(new Transaction("transfer", amount, "to: " + recipient.getUsername()));
        return "Перевод на сумму " + amount + " выполнен пользователю " + recipient.getUsername() + ".";
    }
    public boolean hasCategory(String categoryName) {
        return categories.containsKey(categoryName);
    }

}