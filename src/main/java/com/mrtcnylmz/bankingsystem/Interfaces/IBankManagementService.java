package com.mrtcnylmz.bankingsystem.Interfaces;

import com.mrtcnylmz.bankingsystem.Exceptions.BankExistException;
import com.mrtcnylmz.bankingsystem.Requests.BankRegisterRequest;
import com.mrtcnylmz.bankingsystem.Response.BankCreateResponse;

public interface IBankManagementService {
	public BankCreateResponse create(BankRegisterRequest request) throws BankExistException;
}
