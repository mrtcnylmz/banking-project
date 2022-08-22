package com.mrtcnylmz.bankingsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mrtcnylmz.bankingsystem.Exceptions.BankExistException;
import com.mrtcnylmz.bankingsystem.Interfaces.IBankManagementService;
import com.mrtcnylmz.bankingsystem.Requests.BankRegisterRequest;
import com.mrtcnylmz.bankingsystem.Response.MessageResponse;

@RestController
public class BankManagementController {
	
	@Autowired
	private IBankManagementService bankManagementService;
	
	//Bank creation service.
	@PostMapping("/banks")
	public ResponseEntity<?> create(@RequestBody BankRegisterRequest request) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = bankManagementService.create(request);
			httpStatus = HttpStatus.OK;
			
		} catch (BankExistException e) {
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
