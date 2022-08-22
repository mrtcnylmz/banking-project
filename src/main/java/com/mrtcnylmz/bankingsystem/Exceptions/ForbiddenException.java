package com.mrtcnylmz.bankingsystem.Exceptions;

@SuppressWarnings("serial")
public class ForbiddenException extends Exception {
    public ForbiddenException(String errorMessage) {
        super(errorMessage);
    }
}
