package com.symbo.assignment.service.api;

import com.symbo.assignment.exception.AccountNotFoundException;
import com.symbo.assignment.exception.InsufficientBalanceException;
import com.symbo.assignment.exception.InvalidInputException;
import com.symbo.assignment.model.dto.AccountDTO;
import com.symbo.assignment.model.dto.DepositDTO;
import com.symbo.assignment.model.dto.WithdrawalDTO;
import com.symbo.assignment.model.response.AccountEnquiryResponse;
import com.symbo.assignment.model.response.CreateAccountResponse;
import com.symbo.assignment.model.response.DepositMoneyResponse;
import com.symbo.assignment.model.response.WithdrawMoneyResponse;

public interface ITransactionService {
    CreateAccountResponse getAccountNumber(AccountDTO accountdto) throws InvalidInputException;

    DepositMoneyResponse depositMoney(DepositDTO depositDTO) throws AccountNotFoundException;

    WithdrawMoneyResponse withdrawMoney(WithdrawalDTO withdrawalDTO) throws AccountNotFoundException, InsufficientBalanceException;

    AccountEnquiryResponse getAccountDetails(String accountNumber) throws AccountNotFoundException;
}
