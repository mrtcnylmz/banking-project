package com.mrtcnylmz.bankingsystem.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDepositResponse {
	private boolean success;
	private String message;
	private double balance;
}
