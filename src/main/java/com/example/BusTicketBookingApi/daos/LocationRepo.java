package com.example.BusTicketBookingApi.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.BusTicketBookingApi.models.Location;


public interface LocationRepo extends JpaRepository<Location, Integer>{
	
	Optional<Location> findByLocationName(String locationName);
	
	@Query("SELECT locationName FROM Location order by locationName")
	List<String> findAllProjectedByLocationName();
	
	@Query("FROM Location where locationName like %:name% order by locationName")
	List<Location> findAllByLocationNameIgnoreCase(String name);
	
	
}
