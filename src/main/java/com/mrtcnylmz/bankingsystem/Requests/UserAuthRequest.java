package com.mrtcnylmz.bankingsystem.Requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthRequest {
	private String username;
	private String password;
}
