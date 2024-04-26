package com.example.budgetpal.ui.login;

public class User {
    private int id;
    private String name;
    private String pin;
    private String loginMethod;

    // Constructors, getters, and setters
    public User(int id, String name, String pin, String loginMethod) {
        this.id = id;
        this.name = name;
        this.pin = pin;
        this.loginMethod = loginMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }
}
