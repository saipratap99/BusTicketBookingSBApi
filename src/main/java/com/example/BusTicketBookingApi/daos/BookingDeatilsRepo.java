package com.example.BusTicketBookingApi.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.Schedule;

public interface BookingDeatilsRepo extends JpaRepository<BookingDetails, Integer>{
	List<BookingDetails> findAllByUserIdOrderByDojDescTimeDescBookedAtDesc(int id);
	
	List<BookingDetails> findByScheduleAndStatus(Schedule schedule, String status);
	
	List<BookingDetails> findAllByOrderByDojDescTimeDescBookedAtDesc();
	
	@Query("FROM BookingDetails where schedule.busDetails.operator.operator = :operator order by doj desc, time desc, bookedAt desc")
	List<BookingDetails> findAllOrderByDojDescTimeDescBookedAtDesc(String operator);
	
	@Query("SELECT count(*) FROM BookingDetails where status != 'HOLD'")	
	Integer countAllBookings();
	
	@Query("SELECT count(*) FROM BookingDetails where schedule.busDetails.operator.operator = :operator and status != 'HOLD' group by schedule.busDetails.operator.operator")
	Integer countAllBookingsOfOperator(String operator);
	
	@Query("SELECT count(*) FROM BookingDetails where status = :status")
	Integer countAllBookingsForStatus(String status);
	
	@Query("SELECT count(*) FROM BookingDetails where schedule.busDetails.operator.operator = :operator and status = :status group by schedule.busDetails.operator.operator")
	Integer countAllBookingsOfOperatorForStatus(String operator, String status);
	
}
