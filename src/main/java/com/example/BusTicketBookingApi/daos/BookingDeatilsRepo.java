package com.example.BusTicketBookingApi.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.Schedule;

public interface BookingDeatilsRepo extends JpaRepository<BookingDetails, Integer>{
	List<BookingDetails> findAllByUserIdOrderByDojDesc(int id);
	
	List<BookingDetails> findByScheduleAndStatus(Schedule schedule, String status);
	
	@Query("SELECT count(*) FROM BookingDetails where status != 'HOLD'")	
	int countAllBookings();
	
	@Query("SELECT count(*) FROM BookingDetails where schedule.busDetails.operator.operator = :operator and status != 'HOLD' group by schedule.busDetails.operator.operator")
	int countAllBookingsOfOperator(String operator);
	
	@Query("SELECT count(*) FROM BookingDetails where status = :status")
	int countAllBookingsForStatus(String status);
	
	@Query("SELECT count(*) FROM BookingDetails where schedule.busDetails.operator.operator = :operator and status = :status group by schedule.busDetails.operator.operator")
	int countAllBookingsOfOperatorForStatus(String operator, String status);
	
}
