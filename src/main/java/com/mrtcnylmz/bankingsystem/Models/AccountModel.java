package com.mrtcnylmz.bankingsystem.Models;

import java.time.Instant;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Alias(value = "Account")
public class AccountModel {
	private int id;
    private int userId;
    private int bankId;
    private long number;
    private String type;
    private double balance;
    private Instant creationDate;
    private Instant lastUpdate;
    private boolean isDeleted;
}
