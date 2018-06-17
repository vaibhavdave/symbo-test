package com.symbo.assignment.model.response;

import java.util.ArrayList;
import java.util.List;

public class AccountEnquiryResponse {
    private String accountNumber;
    private String firstname;
    private String lastname;
    private String email;
    private Long balance;
    private List<String> errors;

    public AccountEnquiryResponse(){
        errors = new ArrayList<>();
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
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
