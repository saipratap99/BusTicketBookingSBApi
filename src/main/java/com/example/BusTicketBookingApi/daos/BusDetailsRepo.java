package com.example.BusTicketBookingApi.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BusDetails;

public interface BusDetailsRepo extends JpaRepository<BusDetails, Integer> {
	List<BusDetails> findAll();
	
	@Query("SELECT count(*) FROM BusDetails")
	Integer getAllBuses();
	
	@Query("SELECT count(*) FROM BusDetails where operator.operator = :operator group by operator.operator")
	Integer getAllBusesOfOperator(String operator);

	@Query("SELECT count(*) FROM BusDetails where operator.operator = :operator and isDeleted = false group by operator.operator")
	Integer getAllRunningBusesOfOperator(String operator);

	@Query("SELECT count(*) FROM BusDetails where isDeleted = false")
	Integer getAllRunningBuses();
	
	@Query("SELECT count(*) FROM BusDetails where operator.operator = :operator and isDeleted = true group by operator.operator")
	Integer getAllStoppedBusesOfOperator(String operator);

	@Query("SELECT count(*) FROM BusDetails where isDeleted = true")
	Integer getAllStoppedBuses();
}
