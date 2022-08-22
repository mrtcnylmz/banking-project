package com.mrtcnylmz.bankingsystem.Exceptions;

@SuppressWarnings("serial")
public class BankExistException extends Exception {
    public BankExistException(String errorMessage) {
        super(errorMessage);
    }
}
