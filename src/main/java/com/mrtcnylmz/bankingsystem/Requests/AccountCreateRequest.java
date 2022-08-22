package com.mrtcnylmz.bankingsystem.Requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateRequest {
	private int bankId;
	private String type;

}
