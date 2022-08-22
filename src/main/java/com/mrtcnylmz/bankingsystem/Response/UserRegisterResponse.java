package com.mrtcnylmz.bankingsystem.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponse {
	private boolean success;
	private String message;
	private UserResponse user;
}