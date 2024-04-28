package com.example.budgetpal.ui;
public class BankAccount {
    private int accountId;
    private String accountName;
    private String addedOn;
    private String lastUpdatedOn;

    public BankAccount(int accountId, String accountName, String addedOn, String lastUpdatedOn) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.addedOn = addedOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    // Getters and setters
    public int getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }
}
