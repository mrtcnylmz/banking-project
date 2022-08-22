package com.mrtcnylmz.bankingsystem.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mrtcnylmz.bankingsystem.Interfaces.IAuthService;
import com.mrtcnylmz.bankingsystem.Models.UserModel;
import com.mrtcnylmz.bankingsystem.Repository.UserRepository;
import com.mrtcnylmz.bankingsystem.Requests.UserAuthRequest;
import com.mrtcnylmz.bankingsystem.Requests.UserEnableRequest;
import com.mrtcnylmz.bankingsystem.Requests.UserRegisterRequest;
import com.mrtcnylmz.bankingsystem.Response.UserAuthResponse;
import com.mrtcnylmz.bankingsystem.Response.UserEnableResponse;
import com.mrtcnylmz.bankingsystem.Response.UserRegisterResponse;
import com.mrtcnylmz.bankingsystem.Response.UserResponse;
import com.mrtcnylmz.bankingsystem.Util.JWTUtil;

@Component
public class AuthService implements IAuthService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Override
	public UserAuthResponse auth(UserAuthRequest request){
		
		//Authentication with credentials.
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		//Generate Token
		final String token = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(request.getUsername()));
		
		UserAuthResponse response = UserAuthResponse.builder()
				.success(true)
				.message("Logged-In Successfully")
				.token(token)
				.build();
		
		return response;
	}

	@Override
	public UserRegisterResponse register(UserRegisterRequest request) throws BadCredentialsException {
		
		//Input field Checks
		if (request.getUsername() == null || request.getUsername().equals("") || 
				request.getEmail() == null || request.getEmail().equals("") || 
				request.getPassword() == null || request.getPassword().equals("")) {
			throw new BadCredentialsException("Username, Email or Password can't be empty!");
			
		}else if (userRepository.selectUserWithUsername(request.getUsername()) != null) {
			throw new BadCredentialsException("Given username already used : " + request.getUsername());
			
		}else if (userRepository.selectUserWithEmail(request.getEmail()) != null) {
			throw new BadCredentialsException("Given email already used : " + request.getEmail());
			
		}else {
			UserModel user = UserModel.builder()
					.username(request.getUsername())
					.email(request.getEmail())
					.password(new BCryptPasswordEncoder().encode(request.getPassword()))
					.enabled(false)
					.authorities("CREATE_ACCOUNT")
					.build();
			
			//Insert user data to database.
			userRepository.insertUser(user);
			
			//Create response userModel
			UserResponse userResponse = UserResponse.builder()
					.username(request.getUsername())
					.email(request.getEmail())
					.build();
			
			//Prepare response.
			UserRegisterResponse response = UserRegisterResponse.builder()
					.success(true)
					.message("Created Successfully.")
					.user(userResponse)
					.build();
			
			return response;
		}
	}

	@Override
	public UserEnableResponse enable(int id, UserEnableRequest request) throws Exception {
		
		UserModel user = userRepository.selectUserWithId(id);
		
		if (user == null) {
			throw new Exception("User does not exist: ");
		}
		if (user.isEnabled() == request.isEnable()) {
			if (user.isEnabled()) {
				throw new Exception("User already Enabled");
				
			}else {
				throw new Exception("User already Disabled");
			}
		}
		
		userRepository.updateUserEnabledWithId(id, request.isEnable());
		
		UserEnableResponse response = UserEnableResponse.builder()
				.success(true)
				.message((request.isEnable())?"User Enabled":"User Disabled")
				.build();
		
		return response;
	}
}
