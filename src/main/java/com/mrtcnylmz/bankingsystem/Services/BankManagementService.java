package com.mrtcnylmz.bankingsystem.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mrtcnylmz.bankingsystem.Exceptions.BankExistException;
import com.mrtcnylmz.bankingsystem.Interfaces.IBankManagementService;
import com.mrtcnylmz.bankingsystem.Models.BankModel;
import com.mrtcnylmz.bankingsystem.Repository.BankRepository;
import com.mrtcnylmz.bankingsystem.Requests.BankRegisterRequest;
import com.mrtcnylmz.bankingsystem.Response.BankCreateResponse;

@Component
public class BankManagementService implements IBankManagementService {
	
	@Autowired
	private BankRepository bankRepository;
	
	@Override
	public BankCreateResponse create(BankRegisterRequest request) throws BankExistException {
		
		if (bankRepository.selectBankWithName(request.getName()) != null) {
			throw new BankExistException("Given name is already being used : " + request.getName());
		}
		
		BankModel bankModel = BankModel.builder()
				.name(request.getName())
				.build();
		
		bankRepository.insertBank(bankModel);
		
		bankModel.setId(bankRepository.selectBankWithName(request.getName()).getId());
		
		BankCreateResponse response = BankCreateResponse.builder()
				.success(true)
				.message("Created Successfully.")
				.bank(bankModel)
				.build();
		
		return response;
	}
}
