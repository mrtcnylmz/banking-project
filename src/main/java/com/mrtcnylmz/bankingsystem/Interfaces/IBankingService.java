package com.mrtcnylmz.bankingsystem.Interfaces;

import com.mrtcnylmz.bankingsystem.Requests.AccountCreateRequest;
import com.mrtcnylmz.bankingsystem.Requests.AccountDepositRequest;
import com.mrtcnylmz.bankingsystem.Requests.AccountTransferRequest;
import com.mrtcnylmz.bankingsystem.Response.AccountCreateResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountDepositResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountDetailResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountRemoveResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountTransferResponse;

public interface IBankingService {
	public AccountCreateResponse create(AccountCreateRequest request) throws Exception;
	public AccountDetailResponse details(int id) throws Exception;
	public AccountRemoveResponse remove(int id) throws Exception;
	public AccountDepositResponse deposit(int id, AccountDepositRequest request) throws Exception;
	public AccountTransferResponse transfer(int id, AccountTransferRequest request) throws Exception;
}
