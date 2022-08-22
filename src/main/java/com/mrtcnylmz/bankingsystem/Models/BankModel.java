package com.mrtcnylmz.bankingsystem.Models;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Alias(value = "Bank")
public class BankModel {
	private int id;
	private String name;
}
