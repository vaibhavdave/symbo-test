package com.symbo.assignment.model.response;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountResponse {
    String accountNumber;
    List<String> errors;

    public CreateAccountResponse(){
        errors = new ArrayList<>();
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<String> getErrors() {
        if(errors==null){
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
