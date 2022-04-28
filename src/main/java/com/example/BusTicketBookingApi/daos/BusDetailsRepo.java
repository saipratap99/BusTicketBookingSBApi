package com.example.BusTicketBookingApi.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BusTicketBookingApi.models.BusDetails;

public interface BusDetailsRepo extends JpaRepository<BusDetails, Integer> {
	List<BusDetails> findAll();
}
