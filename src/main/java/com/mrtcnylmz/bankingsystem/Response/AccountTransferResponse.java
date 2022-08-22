package com.mrtcnylmz.bankingsystem.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransferResponse {
	private boolean success;
	private String message;
}
