package com.mrtcnylmz.bankingsystem.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthResponse {
	private boolean success;
	private String message;
	private String token;
}
