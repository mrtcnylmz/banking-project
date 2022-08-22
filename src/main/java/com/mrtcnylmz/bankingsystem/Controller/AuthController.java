package com.mrtcnylmz.bankingsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mrtcnylmz.bankingsystem.Interfaces.IAuthService;
import com.mrtcnylmz.bankingsystem.Requests.UserAuthRequest;
import com.mrtcnylmz.bankingsystem.Requests.UserEnableRequest;
import com.mrtcnylmz.bankingsystem.Requests.UserRegisterRequest;
import com.mrtcnylmz.bankingsystem.Response.MessageResponse;

@RestController
public class AuthController {
	
	@Autowired
	private IAuthService authService;
	
	//Auth path for user login.
	@PostMapping("/auth")
	public ResponseEntity<?> auth(@RequestBody UserAuthRequest request) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = authService.auth(request);
			httpStatus = HttpStatus.OK;
			
		} catch (BadCredentialsException e) {
			response = MessageResponse.builder()
					.success(false)
					.message("Log-In Failure: " + e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.UNAUTHORIZED;
			
		}catch (DisabledException e) {
			response = MessageResponse.builder()
					.success(false)
					.message("Log-In Failure: " + e.getLocalizedMessage())
					.build();
			httpStatus = HttpStatus.FORBIDDEN;
		}
		return ResponseEntity
				.status(httpStatus)
				.body(response);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
		 
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = authService.register(request);
			httpStatus = HttpStatus.CREATED;
		} catch (BadCredentialsException e) {
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
	
	@PostMapping("/users/{id}")
	public ResponseEntity<?> enable(@PathVariable int id ,@RequestBody UserEnableRequest request) {
		
		Object response;
		HttpStatus httpStatus;
		
		try {
			response = authService.enable(id, request);
			httpStatus = HttpStatus.OK;
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
