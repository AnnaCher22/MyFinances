package org.example;

import java.io.Serializable;

class BudgetCategory implements Serializable {
    private String name;
    private double budgetLimit;
    private double spent;

    public BudgetCategory(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
        this.spent = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getRemainingBudget() {
        return budgetLimit - spent;
    }

    public double getSpent() {
        return spent;
    }

    public void addExpense(double amount) {
        spent += amount;
    }
}

