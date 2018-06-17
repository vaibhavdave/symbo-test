package com.symbo.assignment.repository;

import com.symbo.assignment.model.bo.TransactionBO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class TransactionRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public TransactionBO saveTransaction(TransactionBO transactionBO) {
        transactionBO.setId(new ObjectId().toString());
        transactionBO.setDate(LocalDate.now());
        transactionBO.setTime(LocalTime.now());
        mongoTemplate.save(transactionBO);
        return transactionBO;
    }
}
