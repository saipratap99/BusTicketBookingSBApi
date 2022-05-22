package com.example.BusTicketBookingApi.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer>{

	List<Schedule> findByBusDetails(BusDetails busDetails);
	
	@Query("from Schedule where serviceDetails.id = :serviceId and weekDay = :week order by departureTime")
	List<Schedule> findAllByServiceDetailsAndWeekDay(int serviceId, int week);

}
