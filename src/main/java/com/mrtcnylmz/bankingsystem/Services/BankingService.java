package com.mrtcnylmz.bankingsystem.Services;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mrtcnylmz.bankingsystem.Exceptions.ForbiddenException;
import com.mrtcnylmz.bankingsystem.Interfaces.IBankingService;
import com.mrtcnylmz.bankingsystem.Models.AccountModel;
import com.mrtcnylmz.bankingsystem.Repository.AccountRepository;
import com.mrtcnylmz.bankingsystem.Repository.BankRepository;
import com.mrtcnylmz.bankingsystem.Repository.UserRepository;
import com.mrtcnylmz.bankingsystem.Requests.AccountCreateRequest;
import com.mrtcnylmz.bankingsystem.Requests.AccountDepositRequest;
import com.mrtcnylmz.bankingsystem.Requests.AccountTransferRequest;
import com.mrtcnylmz.bankingsystem.Response.AccountCreateResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountDepositResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountDetailResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountRemoveResponse;
import com.mrtcnylmz.bankingsystem.Response.AccountTransferResponse;

@Component
public class BankingService implements IBankingService{
	
    @Autowired
    private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BankRepository bankRepository;
	
	@Autowired
	private CurrencyExchangeService currencyExchangeService;
	
	@Autowired
	private KafkaTemplate<String,String> kafkaProducer;
	
	
	@Override
	public AccountCreateResponse create(AccountCreateRequest request) throws Exception {
		
		if (bankRepository.selectBankWithId(request.getBankId()) == null) {
			throw new Exception("This bank does not exist, Id: " + request.getBankId());
		}
		
		if (!(request.getType().equals("TRY") || request.getType().equals("USD") || request.getType().equals("GAU"))) {
			throw new Exception("Invalid Account Type: " + request.getType());
		}
		
		long randomNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		
		AccountModel account = AccountModel.builder()
				.bankId(request.getBankId())
				.type(request.getType())
				.number(randomNumber)
				.creationDate(ZonedDateTime.now().toInstant())
				.lastUpdate(ZonedDateTime.now().toInstant())
				.userId(userRepository.selectUserWithUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId())
				.build();
		
		accountRepository.insertAccount(account);
		
		AccountCreateResponse response = AccountCreateResponse.builder()
				.success(true)
				.message("Account Created")
				.account(account)
				.build();
		return response;
	}

	@Override
	public AccountDetailResponse details(int id) throws Exception {
		
		AccountModel account = accountRepository.selectAccountWithId(id);
		
		if (account == null || account.isDeleted()) {
			throw new Exception("This account does not exist.");
		}
		
		if (!(userRepository.selectUserWithId(account.getUserId()).getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))) {
			throw new ForbiddenException("Access Denied");
		}
		
		AccountDetailResponse response = AccountDetailResponse.builder()
				.success(true)
				.account(account)
				.lastUpdateDate(account.getLastUpdate())
				.build();
		
		return response;
	}

	@Override
	public AccountRemoveResponse remove(int id) throws Exception {
		
		AccountModel account = accountRepository.selectAccountWithId(id);
		
		if (account == null || account.isDeleted()) {
			throw new Exception("This account does not exist.");
		}
		
		if (!(userRepository.selectUserWithId(account.getUserId()).getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))) {
			throw new ForbiddenException("Access Denied");
		}
		
		accountRepository.updateAccountIsDeletedWithId(id);
		accountRepository.updateAccountLastUpdateWithId(id, ZonedDateTime.now().toInstant());
		
		AccountRemoveResponse response = AccountRemoveResponse.builder()
				.success(true)
				.message("Account Deleted")
				.build();
		
		return response;
	}

	@Override
	public AccountDepositResponse deposit(int id, AccountDepositRequest request) throws Exception {
		
		AccountModel account = accountRepository.selectAccountWithId(id);
		
		if (account == null || account.isDeleted()) {
			throw new Exception("This account does not exist.");
		}
		
		if (!(userRepository.selectUserWithId(account.getUserId()).getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))) {
			throw new ForbiddenException("Access Denied");
		}
		
		double newBalance = account.getBalance() + request.getAmount();
		
		accountRepository.updateAccountBalanceWithId(id, newBalance);
		accountRepository.updateAccountLastUpdateWithId(id, ZonedDateTime.now().toInstant());
		
		String log = account.getId() + " , " + request.getAmount() + " : " + "deposited";
		kafkaProducer.send("logs", log);
		
		AccountDepositResponse response = AccountDepositResponse.builder()
				.success(true)
				.message("Deposit Successfully")
				.balance(newBalance)
				.build();
		return response;
	}

	@Transactional(isolation =Isolation.SERIALIZABLE)
	@Override
	public AccountTransferResponse transfer(int id, AccountTransferRequest request) throws Exception {
		
		//Accounts
		AccountModel senderAccount = accountRepository.selectAccountWithId(id);
		AccountModel receiverAccount = accountRepository.selectAccountWithId(request.getReceiverAccountId());
		double eftFee = 0; //EFT Fee
		
		if (senderAccount == null || senderAccount.isDeleted()) {
			throw new Exception("This account does not exist.");
			
		}else if (!(userRepository.selectUserWithId(senderAccount.getUserId()).getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))) {
			throw new ForbiddenException("Access Denied");
			
		}else if (receiverAccount == null || receiverAccount.isDeleted()) {
			throw new Exception("Receiver account does not exist.");
		}
		
		//EFT Fee adjusment.
		if (senderAccount.getBankId() != receiverAccount.getBankId()) {
			if (senderAccount.getType().equals("TRY")) {
				eftFee = 3;
			}else if (senderAccount.getType().equals("USD")) {
				eftFee = 1;
			}
		}
		
		//Balance checks
		if (senderAccount.getBalance() < (request.getAmount() + eftFee)) {
			throw new Exception("Insufficient balance");
		}
		
		//Same type transfer.
		if (senderAccount.getType().equals(receiverAccount.getType())) {
			
			accountRepository.updateAccountBalanceWithId(id, senderAccount.getBalance() - (request.getAmount() + eftFee));
			accountRepository.updateAccountBalanceWithId(request.getReceiverAccountId(), receiverAccount.getBalance() + request.getAmount());
			
			accountRepository.updateAccountLastUpdateWithId(id, ZonedDateTime.now().toInstant());
			accountRepository.updateAccountLastUpdateWithId(request.getReceiverAccountId(), ZonedDateTime.now().toInstant());
			
		}else {	//Exchange required transfer.
			
			double exchangedAmount = currencyExchangeService.exchange(senderAccount.getType(), receiverAccount.getType(), request.getAmount());
			
			accountRepository.updateAccountBalanceWithId(id, senderAccount.getBalance() - (request.getAmount() + eftFee));
			accountRepository.updateAccountBalanceWithId(receiverAccount.getId(), receiverAccount.getBalance() + exchangedAmount);
			
			accountRepository.updateAccountLastUpdateWithId(id, ZonedDateTime.now().toInstant());
			accountRepository.updateAccountLastUpdateWithId(request.getReceiverAccountId(), ZonedDateTime.now().toInstant());
		}
		
		//Kafka logs
		String log = request.getAmount() + " , " + senderAccount.getId() + " to " + receiverAccount.getId() + " : transferred";
		kafkaProducer.send("logs", log);
		
		AccountTransferResponse response = AccountTransferResponse.builder()
				.success(true)
				.message("Success")
				.build();
		
		return response;
	}
}
