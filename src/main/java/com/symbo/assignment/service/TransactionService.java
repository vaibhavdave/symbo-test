package com.symbo.assignment.service;

import com.symbo.assignment.exception.AccountNotFoundException;
import com.symbo.assignment.exception.InsufficientBalanceException;
import com.symbo.assignment.exception.InvalidInputException;
import com.symbo.assignment.model.bo.AccountBO;
import com.symbo.assignment.model.bo.TransactionBO;
import com.symbo.assignment.model.dto.AccountDTO;
import com.symbo.assignment.model.dto.DepositDTO;
import com.symbo.assignment.model.dto.WithdrawalDTO;
import com.symbo.assignment.model.enums.TransactionType;
import com.symbo.assignment.model.response.AccountEnquiryResponse;
import com.symbo.assignment.model.response.CreateAccountResponse;
import com.symbo.assignment.model.response.DepositMoneyResponse;
import com.symbo.assignment.model.response.WithdrawMoneyResponse;
import com.symbo.assignment.repository.AccountRepository;
import com.symbo.assignment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public CreateAccountResponse getAccountNumber(AccountDTO accountdto) throws InvalidInputException {

        AccountBO accountBO = new AccountBO();
        accountBO.setFirstname(accountdto.getFirstname());
        accountBO.setLastname(accountdto.getLastname());
        accountBO.setEmail(accountdto.getEmail());
        accountBO.setBalance(accountdto.getBalance());

        accountBO = accountRepository.createAccount(accountBO);
        CreateAccountResponse response = new CreateAccountResponse();
        response.setAccountNumber(accountBO.getAccountNumber());
        return response;
    }

    public DepositMoneyResponse depositMoney(DepositDTO depositDTO) throws AccountNotFoundException {
        Optional<AccountBO> accountBO = accountRepository.findByAccountNumber(depositDTO.getAccountNumber());

        if(!accountBO.isPresent()){
            throw new AccountNotFoundException("Account number not found :" + depositDTO.getAccountNumber());
        }

        accountBO.get().setBalance(accountBO.get().getBalance() + depositDTO.getMoneyToDeposit());
        accountRepository.updateAccount(accountBO.get());

        TransactionBO transactionBO = new TransactionBO();
        transactionBO.setAccountNumber(depositDTO.getAccountNumber());
        transactionBO.setAmount(depositDTO.getMoneyToDeposit());
        transactionBO.setTransactionType(TransactionType.DEPOSIT);

        transactionBO = transactionRepository.saveTransaction(transactionBO);
        DepositMoneyResponse depositMoneyResponse = new DepositMoneyResponse();
        depositMoneyResponse.setAccountNumber(transactionBO.getAccountNumber());
        depositMoneyResponse.setBalance(accountBO.get().getBalance());
        depositMoneyResponse.setTransactionId(transactionBO.getId());

        return depositMoneyResponse;

    }

    public WithdrawMoneyResponse withdrawMoney(WithdrawalDTO withdrawalDTO) throws AccountNotFoundException, InsufficientBalanceException {
        Optional<AccountBO> accountBO = accountRepository.findByAccountNumber(withdrawalDTO.getAccountNumber());

        if(!accountBO.isPresent()){
            throw new AccountNotFoundException("Account number not found :" + withdrawalDTO.getAccountNumber());
        }

        if(accountBO.get().getBalance() < withdrawalDTO.getMoneyToWithdraw()){
            throw new InsufficientBalanceException("Insufficient balance :" + accountBO.get().getBalance());
        }
        accountBO.get().setBalance(accountBO.get().getBalance() - withdrawalDTO.getMoneyToWithdraw());
        accountRepository.updateAccount(accountBO.get());

        TransactionBO transactionBO = new TransactionBO();
        transactionBO.setAccountNumber(withdrawalDTO.getAccountNumber());
        transactionBO.setAmount(withdrawalDTO.getMoneyToWithdraw());
        transactionBO.setTransactionType(TransactionType.WITHDRAW);

        transactionBO = transactionRepository.saveTransaction(transactionBO);
        WithdrawMoneyResponse withdrawMoneyResponse = new WithdrawMoneyResponse();
        withdrawMoneyResponse.setAccountNumber(transactionBO.getAccountNumber());
        withdrawMoneyResponse.setBalance(accountBO.get().getBalance());
        withdrawMoneyResponse.setTransactionId(transactionBO.getId());

        return withdrawMoneyResponse;

    }

    public AccountEnquiryResponse getAccountDetails(String accountNumber) throws AccountNotFoundException {
        Optional<AccountBO> accountBO = accountRepository.findByAccountNumber(accountNumber);
        if(!accountBO.isPresent()){
            throw new AccountNotFoundException("Account number not found :" + accountNumber);
        }

        AccountEnquiryResponse response = new AccountEnquiryResponse();
        response.setAccountNumber(accountBO.get().getAccountNumber());
        response.setBalance(accountBO.get().getBalance());
        response.setEmail(accountBO.get().getEmail());
        response.setFirstname(accountBO.get().getFirstname());
        response.setLastname(accountBO.get().getLastname());

        return response;
    }

}
