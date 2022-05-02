package com.example.BusTicketBookingApi.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.Location;
import com.example.BusTicketBookingApi.models.Seat;

public interface SeatsRepo extends JpaRepository<Seat, Integer>{
	@Query("FROM Seat where seatingType.id = :seatingTypeId order by seatRow, seatCol")
	List<Seat> findAllBySeatingType(int seatingTypeId);
}
