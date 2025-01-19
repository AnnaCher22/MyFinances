package org.example;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Wallet getWallet() {
        return wallet;
    }
}