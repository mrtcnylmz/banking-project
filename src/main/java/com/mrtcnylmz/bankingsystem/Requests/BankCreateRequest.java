package com.mrtcnylmz.bankingsystem.Requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankCreateRequest {
	private String name;
}
