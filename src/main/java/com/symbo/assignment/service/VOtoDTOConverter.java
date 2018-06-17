package com.symbo.assignment.service;

import com.symbo.assignment.exception.InvalidInputException;
import com.symbo.assignment.model.dto.AccountDTO;
import com.symbo.assignment.model.dto.DepositDTO;
import com.symbo.assignment.model.dto.WithdrawalDTO;
import com.symbo.assignment.model.vo.AccountVO;
import com.symbo.assignment.model.vo.DepositVO;
import com.symbo.assignment.model.vo.WithdrawalVO;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VOtoDTOConverter
{

    @Autowired
    EmailValidator emailValidator;

    public AccountDTO convertAccountVOtoDTO(AccountVO accountVO) throws InvalidInputException {

        if(!isFirstnameValid(accountVO.getFirstname())){
            throw new InvalidInputException("Invalid firstname :" + accountVO.getFirstname());
        }

        if(!isLastnameValid(accountVO.getLastname())){
            throw new InvalidInputException("Invalid lastname :" + accountVO.getLastname());
        }
        
        if(!isValidEmail(accountVO.getEmail())){
            throw new InvalidInputException("Invalid email :" + accountVO.getEmail());
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setFirstname(accountVO.getFirstname());
        accountDTO.setLastname(accountVO.getLastname());
        accountDTO.setEmail(accountVO.getEmail());
        accountDTO.setBalance(accountVO.getOpeningBalance());
        return accountDTO;

    }

    private boolean isValidEmail(String email) {
        return emailValidator.isValid(email,null);
    }

    private boolean isFirstnameValid(String firstname){
        return (firstname==null || firstname.trim().equals(""))?false:true;
    }

    private boolean isLastnameValid(String lastname){
        return (lastname==null || lastname.trim().equals(""))?false:true;
    }

    public DepositDTO convertDepositVOtoDTO(DepositVO depositVO) throws InvalidInputException{

        if(!isValidAmount(depositVO.getMoneyToDeposit())){
            throw new InvalidInputException("Invalid amount to deposit :" + depositVO.getMoneyToDeposit());
        }
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setAccountNumber(depositVO.getAccountNumber());
        depositDTO.setMoneyToDeposit(depositVO.getMoneyToDeposit());
        return  depositDTO;
    }

    public WithdrawalDTO convertWithdrawalVOtoDTO(WithdrawalVO withdrawalVO) throws InvalidInputException{

        if(!isValidAmount(withdrawalVO.getMoneyToWithdraw())){
            throw new InvalidInputException("Invalid amount to deposit :" + withdrawalVO.getMoneyToWithdraw());
        }

        WithdrawalDTO withdrawalDTO = new WithdrawalDTO();
        withdrawalDTO.setAccountNumber(withdrawalVO.getAccountNumber());
        withdrawalDTO.setMoneyToWithdraw(withdrawalVO.getMoneyToWithdraw());
        return  withdrawalDTO;
    }

    private boolean isValidAmount(Long moneyToDeposit) {
        return (moneyToDeposit == null || moneyToDeposit <=0)?false:true;
    }
}
