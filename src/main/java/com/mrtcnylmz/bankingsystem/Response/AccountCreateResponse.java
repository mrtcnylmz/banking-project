package com.mrtcnylmz.bankingsystem.Response;

import com.mrtcnylmz.bankingsystem.Models.AccountModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateResponse {
	private boolean success;
	private String message;
	private AccountModel account;
}
