package com.example.BusTicketBookingApi.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BusTicketBookingApi.models.SeatingType;

public interface SeatingTypeRepo extends JpaRepository<SeatingType, Integer> {
	
	Optional<SeatingType> findBySeating(String seatingType);
}
