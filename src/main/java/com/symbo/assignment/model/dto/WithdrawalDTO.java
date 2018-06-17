package com.symbo.assignment.model.dto;

public class WithdrawalDTO {

    String accountNumber;
    Long moneyToWithdraw;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getMoneyToWithdraw() {
        return moneyToWithdraw;
    }

    public void setMoneyToWithdraw(Long moneyToWithdraw) {
        this.moneyToWithdraw = moneyToWithdraw;
    }
}
