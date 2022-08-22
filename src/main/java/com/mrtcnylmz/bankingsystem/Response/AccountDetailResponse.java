package com.mrtcnylmz.bankingsystem.Response;

import com.mrtcnylmz.bankingsystem.Models.AccountModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDetailResponse {
	private boolean success;
	private AccountModel account;
	private java.time.Instant lastUpdateDate;
}
