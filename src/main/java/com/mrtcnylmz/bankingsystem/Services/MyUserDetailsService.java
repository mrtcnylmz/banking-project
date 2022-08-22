package com.mrtcnylmz.bankingsystem.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mrtcnylmz.bankingsystem.Models.UserModel;
import com.mrtcnylmz.bankingsystem.Repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
    @Autowired
    private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//Get data from database.
		UserModel user = repository.selectUserWithUsername(username);
		
		//User check.
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}
		
		//Is User enabled.
		boolean enabled = repository.selectUserEnabledWithUsername(username);
		
		//Turn "authorities" String into List<GrantedAuthority> with a for loop.
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String[] separated = user.getAuthorities().split(",");
		for (String s: separated) {
			authorities.add(new SimpleGrantedAuthority(s)); 
		}
		
		//Prepare UserDetails.
		UserDetails userDetails =  new org.springframework.security.core.userdetails.User(username, user.getPassword(), enabled, true, true, true, authorities);

		return userDetails;
	}
}
