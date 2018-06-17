package com.symbo.assignment.repository.api;

import com.symbo.assignment.model.bo.TransactionBO;

public interface ITransactionRepository {
    TransactionBO saveTransaction(TransactionBO transactionBO);
}
