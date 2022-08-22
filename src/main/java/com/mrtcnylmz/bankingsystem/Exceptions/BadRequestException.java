package com.mrtcnylmz.bankingsystem.Exceptions;

@SuppressWarnings("serial")
public class BadRequestException extends Exception {
    public BadRequestException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}