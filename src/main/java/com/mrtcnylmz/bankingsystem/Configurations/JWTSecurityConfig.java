package com.mrtcnylmz.bankingsystem.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mrtcnylmz.bankingsystem.Filter.JwtRequestFilter;
import com.mrtcnylmz.bankingsystem.Services.MyUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth
		.userDetailsService(myUserDetailsService)
		.passwordEncoder(encoder());
	}
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.antMatchers(HttpMethod.POST, "/auth")
		.permitAll()
		.antMatchers(HttpMethod.POST, "/register")
		.permitAll()
		.antMatchers(HttpMethod.POST, "/banks")
		.hasAuthority("CREATE_BANK")
		.antMatchers(HttpMethod.POST, "/users/**")
		.hasAuthority("ACTIVATE_DEACTIVATE_USER")
		.antMatchers(HttpMethod.POST, "/accounts")
		.hasAuthority("CREATE_ACCOUNT")
		.antMatchers(HttpMethod.GET, "/accounts/**")
		.authenticated()
		.antMatchers(HttpMethod.POST, "/accounts/**")
		.authenticated()
		.antMatchers(HttpMethod.PATCH, "/accounts/**")
		.authenticated()
		.antMatchers(HttpMethod.DELETE, "/accounts/**")
		.hasAuthority("REMOVE_ACCOUNT")
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
