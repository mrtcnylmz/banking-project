package com.mrtcnylmz.bankingsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mrtcnylmz.bankingsystem.Exceptions.ForbiddenException;
import com.mrtcnylmz.bankingsystem.Interfaces.IBankingService;
import com.mrtcnylmz.bankingsystem.Requests.AccountCreateRequest;
import com.mrtcnylmz.bankingsystem.Requests.AccountDepositRequest;
import com.mrtcnylmz.bankingsystem.Requests.AccountTransferRequest;
import com.mrtcnylmz.bankingsystem.Response.AccountDetailResponse;
import com.mrtcnylmz.bankingsystem.Response.MessageResponse;

@RestController
public class BankingController {
	
	@Autowired
	private IBankingService bankingService;
	
	//Account creation Service.
	@PostMapping("/accounts")
	public ResponseEntity<?> create(@RequestBody AccountCreateRequest request) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = bankingService.create(request);
			httpStatus = HttpStatus.CREATED;
		} catch (Exception e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return ResponseEntity
				.status(httpStatus)
				.body(response);
		
	}
	
	//Account Details
	@GetMapping("/accounts/{id}")
	public ResponseEntity<?> detail(@PathVariable int id) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = (AccountDetailResponse) bankingService.details(id);
			httpStatus = HttpStatus.OK;
			
			return ResponseEntity
					.status(httpStatus)
					.lastModified(((AccountDetailResponse) response).getLastUpdateDate())
					.body(response);
			
		}catch (ForbiddenException e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.FORBIDDEN;
			
		} catch (Exception e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return ResponseEntity
				.status(httpStatus)
				.body(response);
		
	}
	
	//Account delete
	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> remove(@PathVariable int id) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = bankingService.remove(id);
			httpStatus = HttpStatus.OK;
			
		}catch (ForbiddenException e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.FORBIDDEN;
			
		} catch (Exception e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return ResponseEntity
				.status(httpStatus)
				.body(response);
	}
	
	//Account deposit
	@PatchMapping("/accounts/{id}/deposit")
	public ResponseEntity<?> deposit(@PathVariable int id, @RequestBody AccountDepositRequest request) {
		

		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = bankingService.deposit(id, request);
			httpStatus = HttpStatus.OK;
			
		}catch (ForbiddenException e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.FORBIDDEN;
			
		} catch (Exception e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return ResponseEntity
				.status(httpStatus)
				.body(response);
		
	}
	
	//Transfer
	@PatchMapping("/accounts/{id}/transfer")
	public ResponseEntity<?> transfer(@PathVariable int id, @RequestBody AccountTransferRequest request) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = bankingService.transfer(id, request);
			httpStatus = HttpStatus.OK;
			
		}catch (ForbiddenException e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.FORBIDDEN;
			
		} catch (Exception e) {
			response = MessageResponse.builder()
					.success(false)
					.message(e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		
		return ResponseEntity
				.status(httpStatus)
				.body(response);
		
	}
}
