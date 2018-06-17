package com.symbo.assignment.repository;


import com.symbo.assignment.model.bo.AccountBO;
import com.symbo.assignment.model.bo.CounterBO;
import com.symbo.assignment.repository.api.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public class AccountRepository implements IAccountRepository {

    public static final String ACCOUNT_NUMBER_SEQUENCE = "account_number_sequence";
    public static final String COUNTER_KEY = "key";
    public static final String COUNTER_VALUE = "value";
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init(){

        if(!mongoTemplate.collectionExists(CounterBO.class)){
            mongoTemplate.createCollection(CounterBO.class);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(COUNTER_KEY).is(ACCOUNT_NUMBER_SEQUENCE));
       Long count = mongoTemplate.count(query,CounterBO.class);

        if(count==0){
            CounterBO counterBO = new CounterBO();
            counterBO.setKey(ACCOUNT_NUMBER_SEQUENCE);
            counterBO.setValue(0L);
            mongoTemplate.save(counterBO);
        }

    }

    @Override
    public AccountBO createAccount(AccountBO accountBO){

        Query query = new Query();
        query.addCriteria(Criteria.where(COUNTER_KEY).is(ACCOUNT_NUMBER_SEQUENCE));
        Update update = new Update();
        update.inc(COUNTER_VALUE,1);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.returnNew(true);

        CounterBO counterBO = mongoTemplate.findAndModify(query,update,findAndModifyOptions,CounterBO.class);
        accountBO.setAccountNumber(counterBO.getValue().toString());
        accountBO.setDate(LocalDate.now());
        accountBO.setTime(LocalTime.now());
        mongoTemplate.insert(accountBO);
        return accountBO;
    }

    @Override
    public Optional<AccountBO> findByAccountNumber(String accountNumber) {

        AccountBO accountBO = mongoTemplate.findById(accountNumber,AccountBO.class);
        if(accountBO==null){
           return Optional.empty();
        }
        return Optional.of(accountBO);
    }


    @Override
    public void updateAccount(AccountBO accountBO) {
        mongoTemplate.save(accountBO);
    }
}
