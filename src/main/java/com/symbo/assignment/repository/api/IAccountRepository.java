package com.symbo.assignment.repository.api;

import com.symbo.assignment.model.bo.AccountBO;

import java.util.Optional;

public interface IAccountRepository {
    AccountBO createAccount(AccountBO accountBO);

    Optional<AccountBO> findByAccountNumber(String accountNumber);

    void updateAccount(AccountBO accountBO);
}
