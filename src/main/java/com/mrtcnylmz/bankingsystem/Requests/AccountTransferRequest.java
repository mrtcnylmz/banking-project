package com.mrtcnylmz.bankingsystem.Requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransferRequest {
	private double amount;
	private int receiverAccountId;
}
