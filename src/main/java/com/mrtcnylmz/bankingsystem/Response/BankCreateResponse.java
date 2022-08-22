package com.mrtcnylmz.bankingsystem.Response;

import com.mrtcnylmz.bankingsystem.Models.BankModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankCreateResponse {
	private boolean success;
	private String message;
	private BankModel bank;
}
