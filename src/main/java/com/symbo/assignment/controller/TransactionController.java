package com.symbo.assignment.controller;

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
import com.symbo.assignment.model.vo.AccountVO;
import com.symbo.assignment.model.vo.DepositVO;
import com.symbo.assignment.model.vo.WithdrawalVO;
import com.symbo.assignment.service.VOtoDTOConverter;
import com.symbo.assignment.service.api.ITransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transactionService/v1")
@Api(value="TransactionService", description="Operations pertaining to banking services")
public class TransactionController {

    @Autowired
    MongoTemplate template;

    @Autowired
    VOtoDTOConverter valueObjectToDtoConverter;

    @Autowired
    ITransactionService transactionService;

    @ApiOperation(value = "ping service",response = String.class, nickname = "Ping")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @RequestMapping(value = "/ping",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<String> ping(){
    return ResponseEntity.ok("{\"status\" : \"success\"}");
    }

    @ApiOperation(value = "getAccountNumber",response = CreateAccountResponse.class, nickname = "Create Account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created account"),
            @ApiResponse(code = 400, message = "Invalid inputs to create account"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @RequestMapping(value = "/accounts",method = RequestMethod.POST,produces = "application/json",consumes="application/json")
    public ResponseEntity<CreateAccountResponse> getAccountNumber(@RequestBody AccountVO accountVO){
        CreateAccountResponse response =  null;
        try {
            AccountDTO accountDTO = valueObjectToDtoConverter.convertAccountVOtoDTO(accountVO);
            response = transactionService.getAccountNumber(accountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (InvalidInputException e) {
            response = new CreateAccountResponse();
            response.getErrors().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e){
            response = new CreateAccountResponse();
            response.getErrors().add("Error while processing request for account creation." +e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }

    }

    @ApiOperation(value = "depositMoney",response = DepositMoneyResponse.class, nickname = "Deposit Money into Account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deposited money into account"),
            @ApiResponse(code = 400, message = "Invalid inputs to deposit into account"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @RequestMapping(value = "/accounts/deposits",method = RequestMethod.POST,produces = "application/json",consumes="application/json")
    public ResponseEntity<DepositMoneyResponse> depositMoney(@RequestBody DepositVO depositVO) {
        DepositMoneyResponse response = null;
        try {
            DepositDTO depositDTO = valueObjectToDtoConverter.convertDepositVOtoDTO(depositVO);
            response = transactionService.depositMoney(depositDTO);
            return ResponseEntity.ok(response);

        } catch (InvalidInputException | AccountNotFoundException e) {
            response = new DepositMoneyResponse();
            response.getErrors().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response = new DepositMoneyResponse();
            response.getErrors().add("Error while processing request for account creation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }
        @ApiOperation(value = "withdrawMoney",response = WithdrawMoneyResponse.class, nickname = "Withdraw Money from Account")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Successfully withdrawal of money from account"),
                @ApiResponse(code = 400, message = "Invalid inputs to withdraw from account or insufficient balance"),
                @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
        }
        )
        @RequestMapping(value = "/accounts/withdrawal",method = RequestMethod.POST,produces = "application/json",consumes="application/json")
        public ResponseEntity<WithdrawMoneyResponse> withdrawMoney(@RequestBody WithdrawalVO withdrawalVO){
            WithdrawMoneyResponse response =  null;
            try {
                WithdrawalDTO withdrawalDTO = valueObjectToDtoConverter.convertWithdrawalVOtoDTO(withdrawalVO);
                response = transactionService.withdrawMoney(withdrawalDTO);
                return ResponseEntity.ok(response);

            } catch (InvalidInputException | AccountNotFoundException | InsufficientBalanceException e) {
                response = new WithdrawMoneyResponse();
                response.getErrors().add(e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

            } catch (Exception e){
                response = new WithdrawMoneyResponse();
                response.getErrors().add("Error while processing request for account creation");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

            }

    }

    @ApiOperation(value = "enquiry",response = WithdrawMoneyResponse.class, nickname = "Get Account Details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully in retrieving account details"),
            @ApiResponse(code = 400, message = "Invalid inputs to withdraw from account or insufficient balance"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @RequestMapping(value = "/accounts/enquiry/{accountNumber}",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<AccountEnquiryResponse> getAccountDetails(@PathVariable("accountNumber") String accountNumber){
        AccountEnquiryResponse response =  null;
        try {
            response = transactionService.getAccountDetails(accountNumber);
            return ResponseEntity.ok(response);

        } catch ( AccountNotFoundException e) {
            response = new AccountEnquiryResponse();
            response.getErrors().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e){
            response = new AccountEnquiryResponse();
            response.getErrors().add("Error while processing request for account creation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }

    }


}
