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
import com.symbo.assignment.model.exchange.AccountOpenedEvent;
import com.symbo.assignment.model.response.AccountEnquiryResponse;
import com.symbo.assignment.model.response.CreateAccountResponse;
import com.symbo.assignment.model.response.DepositMoneyResponse;
import com.symbo.assignment.model.response.WithdrawMoneyResponse;
import com.symbo.assignment.repository.api.IAccountRepository;
import com.symbo.assignment.repository.TransactionRepository;
import com.symbo.assignment.service.api.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    IAccountRepository IAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    EventDispatcher eventDispatcher;

    @Transactional
    @Override
    public CreateAccountResponse getAccountNumber(AccountDTO accountdto) throws InvalidInputException {

        AccountBO accountBO = new AccountBO();
        accountBO.setFirstname(accountdto.getFirstname());
        accountBO.setLastname(accountdto.getLastname());
        accountBO.setEmail(accountdto.getEmail());
        accountBO.setBalance(accountdto.getBalance());

        accountBO = IAccountRepository.createAccount(accountBO);
        CreateAccountResponse response = new CreateAccountResponse();
        response.setAccountNumber(accountBO.getAccountNumber());

        //eventDispatcher.send(new AccountOpenedEvent(accountBO.getAccountNumber()));
        return response;
    }

    @Override
    public DepositMoneyResponse depositMoney(DepositDTO depositDTO) throws AccountNotFoundException {
        Optional<AccountBO> accountBO = IAccountRepository.findByAccountNumber(depositDTO.getAccountNumber());

        if(!accountBO.isPresent()){
            throw new AccountNotFoundException("Account number not found :" + depositDTO.getAccountNumber());
        }

        accountBO.get().setBalance(accountBO.get().getBalance() + depositDTO.getMoneyToDeposit());
        IAccountRepository.updateAccount(accountBO.get());

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

    @Override
    public WithdrawMoneyResponse withdrawMoney(WithdrawalDTO withdrawalDTO) throws AccountNotFoundException, InsufficientBalanceException {
        Optional<AccountBO> accountBO = IAccountRepository.findByAccountNumber(withdrawalDTO.getAccountNumber());

        if(!accountBO.isPresent()){
            throw new AccountNotFoundException("Account number not found :" + withdrawalDTO.getAccountNumber());
        }

        if(accountBO.get().getBalance() < withdrawalDTO.getMoneyToWithdraw()){
            throw new InsufficientBalanceException("Insufficient balance :" + accountBO.get().getBalance());
        }
        accountBO.get().setBalance(accountBO.get().getBalance() - withdrawalDTO.getMoneyToWithdraw());
        IAccountRepository.updateAccount(accountBO.get());

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

    @Override
    public AccountEnquiryResponse getAccountDetails(String accountNumber) throws AccountNotFoundException {
        Optional<AccountBO> accountBO = IAccountRepository.findByAccountNumber(accountNumber);
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
