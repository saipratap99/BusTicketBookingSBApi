package com.example.BusTicketBookingApi.daos;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BookedSeat;
import com.example.BusTicketBookingApi.models.Seat;

public interface BookedSeatsRepo extends JpaRepository<BookedSeat, Integer>{
	
	@Query("Select seat FROM BookedSeat where schedule_id = :scheduleId and bus_details_id = :busId and doj = :doj and time = :time")
	List<Seat> findAllByTripDetails(int scheduleId, int busId, String doj, String time);

	List<BookedSeat> findAllByBookingDetailsId(int bookingId);
	
}
