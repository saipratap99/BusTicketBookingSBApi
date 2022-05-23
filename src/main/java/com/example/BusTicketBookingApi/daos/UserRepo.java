package com.example.BusTicketBookingApi.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	User findByEmail(String email);
	
	@Query("SELECT count(*) FROM User where role != 'ROLE_ADMIN'")
	Integer countAllUsers();
	
	@Query("SELECT count(*) FROM User where role = 'ROLE_USER'")
	Integer countAllCustomers();
	
	@Query("SELECT count(*) FROM User where role = 'ROLE_OPERATOR'")
	Integer countAllOperators();
	
}
