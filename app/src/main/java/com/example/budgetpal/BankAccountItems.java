package com.example.budgetpal;
public class BankAccountItems {
    private int accountId;
    private String smsContent;
    private String smsType;
    private String smsDate;
    private Double amount;

    public BankAccountItems(int accountId, String content, String smsType, Double amount, String smsDate) {
        this.accountId = accountId;
        this.smsContent = content;
        this.smsType = smsType;
        this.smsDate = smsDate;
        this.amount = amount;
    }

    // Getters and setters
    public int getAccountId() {
        return accountId;
    }
    public String getSmsContent() {
        return smsContent;
    }
    public String getSmsType() {
        return smsType;
    }
    public String getSmsDate() {
        return smsDate;
    }
    public Double getAmount() {
        return amount;
    }
}
