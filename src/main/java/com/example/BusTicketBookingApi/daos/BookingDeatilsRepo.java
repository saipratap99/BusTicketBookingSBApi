package com.example.BusTicketBookingApi.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BusTicketBookingApi.models.BookingDetails;

public interface BookingDeatilsRepo extends JpaRepository<BookingDetails, Integer>{
	List<BookingDetails> findAllByUserId(int id);
}
