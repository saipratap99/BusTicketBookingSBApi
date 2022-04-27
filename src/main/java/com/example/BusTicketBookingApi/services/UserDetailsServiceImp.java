package com.example.BusTicketBookingApi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.MyUserDetails;
import com.example.BusTicketBookingApi.models.User;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

	@Autowired
	UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByEmail(username);
		if(user != null)
			return new MyUserDetails(user);
		else
			throw new UsernameNotFoundException("User not found");
		
	}
	
}
