package com.example.BusTicketBookingApi.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.ServiceDetails;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer>{

	List<Schedule> findByBusDetails(BusDetails busDetails);
	
	List<Schedule> findByServiceDetails(ServiceDetails serviceDetails);
	
	@Query("from Schedule where serviceDetails.id = :serviceId and weekDay = :week and isDeleted = false order by departureTime")
	List<Schedule> findAllByServiceDetailsAndWeekDay(int serviceId, int week);

}
