package com.example.BusTicketBookingApi.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BusTicketBookingApi.models.BookingDetails;

public interface BookingDeatilsRepo extends JpaRepository<BookingDetails, Integer>{
	
}
