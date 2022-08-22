package com.mrtcnylmz.bankingsystem.Repository;

import java.time.Instant;

import org.apache.ibatis.annotations.Mapper;

import com.mrtcnylmz.bankingsystem.Models.AccountModel;

@Mapper
public interface AccountRepository {
	public void insertAccount(AccountModel accountModel);
	public AccountModel selectAccountWithId(int id);
	public int selectUserIdWithId(int id);
	public void updateAccountIsDeletedWithId(int id);
	public void updateAccountBalanceWithId(int id, double balance);
	public void updateAccountLastUpdateWithId(int id, Instant lastUpdateDate);
	public int selectBankIdWithId(int id);
	public boolean selectIsDeletedWithId(int id);
	public boolean isAccountDeletedWithId(int id);
}