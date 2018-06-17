package com.symbo.assignment.controller;

import com.symbo.assignment.config.TestConfig;
import com.symbo.assignment.model.bo.AccountBO;
import com.symbo.assignment.model.bo.CounterBO;
import com.symbo.assignment.model.bo.TransactionBO;
import com.symbo.assignment.model.response.AccountEnquiryResponse;
import com.symbo.assignment.model.response.CreateAccountResponse;
import com.symbo.assignment.model.response.DepositMoneyResponse;
import com.symbo.assignment.model.response.WithdrawMoneyResponse;
import com.symbo.assignment.model.vo.AccountVO;
import com.symbo.assignment.model.vo.DepositVO;
import com.symbo.assignment.model.vo.WithdrawalVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TestConfig.class)
@SpringBootTest
public class TransactionControllerTest {

    public static final String ACCOUNT_NUMBER_SEQUENCE = "account_number_sequence";
    public static final String COUNTER_KEY = "key";
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TransactionController transactionController;

    @Before()
    public void init(){
        mongoTemplate.dropCollection(AccountBO.class);
        mongoTemplate.dropCollection(TransactionBO.class);
        mongoTemplate.dropCollection(CounterBO.class);

        CounterBO counterBO = new CounterBO();
        counterBO.setKey(ACCOUNT_NUMBER_SEQUENCE);
        counterBO.setValue(0L);

        mongoTemplate.save(counterBO);
        Query q = new Query();
        q.addCriteria(Criteria.where(COUNTER_KEY).is(ACCOUNT_NUMBER_SEQUENCE));
        CounterBO  retObj = mongoTemplate.findOne(q,CounterBO.class);
        Assert.assertTrue(retObj!=null && retObj.getValue().equals(0L));
    }

    @Test
    public  void getAccountNumberTest_with_no_errors()  {
        AccountVO accountVO = getAccountVO();
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1",response1.getBody().getAccountNumber());

        ResponseEntity<CreateAccountResponse> response2 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("2",response2.getBody().getAccountNumber());

    }

    @Test
    public  void getAccountNumberTest_with_invalid_email()  {
        AccountVO accountVO = getAccountVO();
        accountVO.setEmail("vaibhav.com");
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertTrue("Status code should be 400",response1.getStatusCode()==HttpStatus.BAD_REQUEST);
        Assert.assertTrue("Should have 1 error",response1.getBody().getErrors().size()==1);


    }


    private AccountVO getAccountVO() {
        AccountVO accountVO = new AccountVO();
        accountVO.setLastname("Dave");
        accountVO.setFirstname("vaibhav");
        accountVO.setEmail("vaibhavdave10990@gmail.com");
        return accountVO;
    }

    @Test
    public  void depositMoney_with_no_errors()  {
        AccountVO accountVO = getAccountVO();
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1",response1.getBody().getAccountNumber());

        DepositVO vo = new DepositVO();
        vo.setMoneyToDeposit(1000L);
        vo.setAccountNumber("1");
        ResponseEntity<DepositMoneyResponse> depositMoneyResponse = transactionController.depositMoney(vo);
        Assert.assertTrue("Balance should have been 1000",depositMoneyResponse.getBody().getBalance()==1000);

        DepositVO vo1 = new DepositVO();
        vo1.setMoneyToDeposit(1000L);
        vo1.setAccountNumber("1");
        ResponseEntity<DepositMoneyResponse> depositMoneyResponse1 = transactionController.depositMoney(vo1);
        Assert.assertTrue("Balance should have been 2000",depositMoneyResponse1.getBody().getBalance()==2000);
    }

    @Test
    public  void depositMoney_with_negative_amount()  {
        AccountVO accountVO = getAccountVO();
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());

        DepositVO vo1 = new DepositVO();
        vo1.setMoneyToDeposit(-1000L);
        vo1.setAccountNumber("1");
        ResponseEntity<DepositMoneyResponse> depositMoneyResponse = transactionController.depositMoney(vo1);
        Assert.assertTrue("1 Error should come",depositMoneyResponse.getBody().getErrors().size()==1);

    }

    @Test
    public  void depositMoney_with_invalid_account_number()  {
        AccountVO accountVO = getAccountVO();
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());

        DepositVO vo1 = new DepositVO();
        vo1.setMoneyToDeposit(1000L);
        vo1.setAccountNumber("3");
        ResponseEntity<DepositMoneyResponse> depositMoneyResponse = transactionController.depositMoney(vo1);
        Assert.assertTrue("1 Error should come",depositMoneyResponse.getBody().getErrors().size()==1);

    }

    @Test
    public  void withdrawMoney_with_invalid_account_number()  {
        AccountVO accountVO = getAccountVO();
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());

        WithdrawalVO vo1 = new WithdrawalVO();
        vo1.setAccountNumber("2");
        vo1.setMoneyToWithdraw(100L);
        ResponseEntity<WithdrawMoneyResponse> withdrawMoneyResponse = transactionController.withdrawMoney(vo1);
        Assert.assertTrue("Status code should be 400",withdrawMoneyResponse.getStatusCode() == HttpStatus.BAD_REQUEST);
        Assert.assertTrue("1 Error should come",withdrawMoneyResponse.getBody().getErrors().size()==1);

    }

    @Test
    public  void withdrawMoney_with_negative_amount()  {
        AccountVO accountVO = getAccountVO();
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());

        WithdrawalVO vo1 = new WithdrawalVO();
        vo1.setAccountNumber("2");
        vo1.setMoneyToWithdraw(100L);
        ResponseEntity<WithdrawMoneyResponse> withdrawMoneyResponse = transactionController.withdrawMoney(vo1);
        Assert.assertTrue("Status code should be 400",withdrawMoneyResponse.getStatusCode() == HttpStatus.BAD_REQUEST);
        Assert.assertTrue("1 Error should come",withdrawMoneyResponse.getBody().getErrors().size()==1);

    }

    @Test
    public  void withdrawMoney_with_valid_amount()  {
        AccountVO accountVO = getAccountVO();
        accountVO.setOpeningBalance(10000L);
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());

        WithdrawalVO vo1 = new WithdrawalVO();
        vo1.setAccountNumber("1");
        vo1.setMoneyToWithdraw(100L);
        ResponseEntity<WithdrawMoneyResponse> withdrawMoneyResponse = transactionController.withdrawMoney(vo1);
        Assert.assertTrue("Status code should be 200",withdrawMoneyResponse.getStatusCode() == HttpStatus.OK);
        Assert.assertTrue("Balance should be 9800",withdrawMoneyResponse.getBody().getBalance() == 9900);

    }

    @Test
    public  void getAccountDetails_with_no_errors()  {
        AccountVO accountVO = getAccountVO();
        accountVO.setOpeningBalance(10000L);
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());


        ResponseEntity<AccountEnquiryResponse> accountEnquiryResponse = transactionController.getAccountDetails("1");
        Assert.assertTrue("Status code should be 200",accountEnquiryResponse.getStatusCode() == HttpStatus.OK);
        Assert.assertTrue("Balance should be 10000",accountEnquiryResponse.getBody().getBalance() == 10000);
        Assert.assertTrue("Account number should be 1",accountEnquiryResponse.getBody().getAccountNumber().equals("1"));

    }

    @Test
    public  void getAccountDetails_with_non_existent_account()  {
        AccountVO accountVO = getAccountVO();
        accountVO.setOpeningBalance(10000L);
        ResponseEntity<CreateAccountResponse> response1 = transactionController.getAccountNumber(accountVO);
        Assert.assertEquals("1", response1.getBody().getAccountNumber());


        ResponseEntity<AccountEnquiryResponse> accountEnquiryResponse = transactionController.getAccountDetails("2");
        Assert.assertTrue("Status code should be 400",accountEnquiryResponse.getStatusCode() == HttpStatus.BAD_REQUEST);
        Assert.assertTrue("Should have 1 error",accountEnquiryResponse.getBody().getErrors().size() == 1);

    }


    }
