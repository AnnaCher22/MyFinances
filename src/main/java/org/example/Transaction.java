package org.example;

import java.io.Serializable;
import java.util.Date;

class Transaction implements Serializable {
    private String type; // Тип операции: income или expense
    private double amount; // Сумма
    private String categoryName; // Название категории (может быть null для доходов)
    private Date date; // Дата транзакции

    public Transaction(String type, double amount, String categoryName) {
        this.type = type;
        this.amount = amount;
        this.categoryName = categoryName;
        this.date = new Date();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Date getDate() {
        return date;
    }
}
