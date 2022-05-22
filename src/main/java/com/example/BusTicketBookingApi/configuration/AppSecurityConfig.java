package com.example.BusTicketBookingApi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.BusTicketBookingApi.filters.JwtRequestFilter;
import com.example.BusTicketBookingApi.utils.PropertiesUtil;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	PropertiesUtil propertiesUtil;
	

	// Authentication	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	// Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.cors();
		
		http.csrf().disable()
			.authorizeHttpRequests()
			.antMatchers("/api/*/admin/**").hasRole("ADMIN")
			.antMatchers("/api/*/bus_details/**", "/api/*/schedule/**", "/api/*/service_details/**").hasAnyRole("ADMIN", "OPERATOR")
			.antMatchers("/api/*/bookings/**").hasAnyRole("ADMIN", "OPERATOR")
			.antMatchers("/api/*/authenticate","/api/*/users/create", "/api/*/users/logout", "/api/*/users/login", "/api/*/users/*/verify/otp/*", "/api/*/users/*/email", "/api/*/users/*/resend/otp", "/api/*/users/auth/refresh-token/*", "/api/*/search/**", "/api/*/seats/schedule/**").permitAll()
			.anyRequest()
			.authenticated();
		
		
		if(propertiesUtil.isSessionsBasedAuth())
			http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		if(propertiesUtil.isJWTBasedAuth())
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		if(propertiesUtil.isJWTBasedAuth()) {	
			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
		
		http
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/users/logout", "POST"))
			.clearAuthentication(true)
			.deleteCookies("refreshToken")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/api/v1/users/logout");

	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	

	
}
