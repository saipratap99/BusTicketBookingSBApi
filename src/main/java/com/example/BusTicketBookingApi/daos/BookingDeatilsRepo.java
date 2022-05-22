package com.example.BusTicketBookingApi.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.Schedule;

public interface BookingDeatilsRepo extends JpaRepository<BookingDetails, Integer>{
	List<BookingDetails> findAllByUserId(int id);
	
	List<BookingDetails> findByScheduleAndStatus(Schedule schedule, String status);
}
