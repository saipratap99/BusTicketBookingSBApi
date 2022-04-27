package com.example.BusTicketBookingApi.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BusTicketBookingApi.models.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	User findByEmail(String email);
}
