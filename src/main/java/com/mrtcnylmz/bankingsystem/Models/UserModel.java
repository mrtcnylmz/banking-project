package com.mrtcnylmz.bankingsystem.Models;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Alias(value = "User")
public class UserModel {
	private int id;
	private String username;
	private String email;
	private String password;
	private boolean enabled;
	private String authorities;
}
