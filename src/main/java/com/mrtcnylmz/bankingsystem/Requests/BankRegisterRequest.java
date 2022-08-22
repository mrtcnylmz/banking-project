package com.mrtcnylmz.bankingsystem.Requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankRegisterRequest {
	private String id;
	private String name;
}
