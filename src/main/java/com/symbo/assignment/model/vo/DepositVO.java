package com.symbo.assignment.model.vo;

public class DepositVO {
    String accountNumber;
    Long moneyToDeposit;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getMoneyToDeposit() {
        return moneyToDeposit;
    }

    public void setMoneyToDeposit(Long moneyToDeposit) {
        this.moneyToDeposit = moneyToDeposit;
    }
}
