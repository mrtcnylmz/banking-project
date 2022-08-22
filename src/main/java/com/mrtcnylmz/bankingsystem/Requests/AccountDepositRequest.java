package com.mrtcnylmz.bankingsystem.Requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDepositRequest {
	private int id;
	private double amount;
}
