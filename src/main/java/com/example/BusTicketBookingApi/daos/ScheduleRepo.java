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

	@Query("SELECT count(*) FROM Schedule")
	Integer countAllSchedules();
	
	@Query("SELECT count(*) FROM Schedule where busDetails.operator.operator = :operator group by busDetails.operator.operator")
	Integer countAllSchedulesOfOperator(String operator);
	
	@Query("SELECT count(*) FROM Schedule where isDeleted = false")
	Integer countAllRunningSchedules();
	
	@Query("SELECT count(*) FROM Schedule where busDetails.operator.operator = :operator and isDeleted = false group by busDetails.operator.operator")
	Integer countAllRunningSchedulesOfOperator(String operator);
	
	@Query("SELECT count(*) FROM Schedule where isDeleted = true")
	Integer countAllStoppedSchedules();
	
	@Query("SELECT count(*) FROM Schedule where busDetails.operator.operator = :operator and isDeleted = true group by busDetails.operator.operator")
	Integer countAllStoppedSchedulesOfOperator(String operator);
	
}
