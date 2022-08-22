package com.mrtcnylmz.bankingsystem.Interfaces;

import com.mrtcnylmz.bankingsystem.Requests.BankCreateRequest;
import com.mrtcnylmz.bankingsystem.Response.BankCreateResponse;

public interface IBankService {
	public BankCreateResponse create(BankCreateRequest request);

}
