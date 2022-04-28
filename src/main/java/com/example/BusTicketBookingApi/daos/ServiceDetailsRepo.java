package com.example.BusTicketBookingApi.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.ServiceDetails;


public interface ServiceDetailsRepo extends JpaRepository<ServiceDetails, Integer> {
	Optional<ServiceDetails> findByServiceName(String serviceName);
	
	@Query("from ServiceDetails where departureLocation.id = :departureId and arrivalLocation.id = :arrivalId")
	List<ServiceDetails> finAllByDepartureLocationAndArrivalLocation(int departureId, int arrivalId);
	
	@Query("SELECT serviceName FROM ServiceDetails order by serviceName")
	List<String> findAllProjectedByServiceName();
}
