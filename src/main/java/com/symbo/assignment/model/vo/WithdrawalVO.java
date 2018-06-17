package com.symbo.assignment.model.vo;

public class WithdrawalVO {
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
