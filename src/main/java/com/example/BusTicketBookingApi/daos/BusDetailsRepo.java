package com.example.BusTicketBookingApi.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BusDetails;

public interface BusDetailsRepo extends JpaRepository<BusDetails, Integer> {
	List<BusDetails> findAll();
	
	@Query("SELECT count(*) FROM BusDetails")
	int getAllBuses();
	
	@Query("SELECT count(*) FROM BusDetails where operator.operator = :operator group by operator.operator")
	int getAllBusesOfOperator(String operator);

	@Query("SELECT count(*) FROM BusDetails where operator.operator = :operator and isDeleted = false group by operator.operator")
	int getAllRunningBusesOfOperator(String operator);

	@Query("SELECT count(*) FROM BusDetails where isDeleted = false")
	int getAllRunningBuses();
	
	@Query("SELECT count(*) FROM BusDetails where operator.operator = :operator and isDeleted = true group by operator.operator")
	int getAllStoppedBusesOfOperator(String operator);

	@Query("SELECT count(*) FROM BusDetails where isDeleted = true")
	int getAllStoppedBuses();
}
