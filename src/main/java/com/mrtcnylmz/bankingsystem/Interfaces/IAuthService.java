package com.mrtcnylmz.bankingsystem.Interfaces;

import com.mrtcnylmz.bankingsystem.Response.UserAuthResponse;
import com.mrtcnylmz.bankingsystem.Response.UserEnableResponse;
import com.mrtcnylmz.bankingsystem.Requests.UserAuthRequest;
import com.mrtcnylmz.bankingsystem.Requests.UserEnableRequest;
import com.mrtcnylmz.bankingsystem.Response.UserRegisterResponse;
import com.mrtcnylmz.bankingsystem.Requests.UserRegisterRequest;

public interface IAuthService {
	public UserAuthResponse auth(UserAuthRequest request);
	public UserRegisterResponse register(UserRegisterRequest request);
	public UserEnableResponse enable(int id ,UserEnableRequest request) throws Exception;
}
