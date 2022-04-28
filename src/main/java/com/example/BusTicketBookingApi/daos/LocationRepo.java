package com.example.BusTicketBookingApi.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.Location;


public interface LocationRepo extends JpaRepository<Location, Integer>{
	
	Optional<Location> findByLocationName(String locationName);
	
	@Query("SELECT locationName FROM Location order by locationName")
	List<String> findAllProjectedByLocationName();
	
//	@Query("SELECT id, location_name FROM locations order by location_name")
//	List<IdAndLocation> findAllProjectedBy();
	
}
