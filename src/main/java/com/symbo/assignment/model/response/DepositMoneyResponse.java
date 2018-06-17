package com.symbo.assignment.model.response;

import java.util.ArrayList;
import java.util.List;

public class DepositMoneyResponse {
    private String accountNumber;
    private Long balance;
    private String transactionId;
    private List<String> errors;

    public DepositMoneyResponse(){
        errors = new ArrayList<>();
    }
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public List<String> getErrors() {
        if(errors==null){
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
