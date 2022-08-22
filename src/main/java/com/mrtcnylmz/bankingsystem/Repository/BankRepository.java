package com.mrtcnylmz.bankingsystem.Repository;

import org.apache.ibatis.annotations.Mapper;

import com.mrtcnylmz.bankingsystem.Models.BankModel;

@Mapper
public interface BankRepository {
	public BankModel selectBankWithId(int id);
	public BankModel selectBankWithName(String name);
	public void insertBank(BankModel bankModel);
}
