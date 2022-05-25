package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.exceptions.InvalidBookingDateException;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.ServiceDetails;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
	
	@Value("${app.max.days}")
	Integer nDays;
		
	@Autowired
	LocationRepo locationRepo;
	
	@Autowired
	ServiceDetailsRepo serviceDetailsRepo;

	@Autowired
	ScheduleRepo scheduleRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	
	@GetMapping("/buses/{depLocation}/{depId}/{arrLocation}/{arrId}/{date}")
	public ResponseEntity<?> availableBuses(@PathVariable String depLocation, @PathVariable int depId, 
											@PathVariable String arrLocation, @PathVariable int arrId,
											@PathVariable Date date, Principal principal) throws InvalidBookingDateException {

		if(basicUtil.isDateBeforeCurrDate(date))
			throw new InvalidBookingDateException("Departure date must not be before current date");
		
		if(basicUtil.isDateAfterNDays(date, nDays))
			throw new InvalidBookingDateException("Departure date must be within "+ nDays +" days from current date");
		
		List<ServiceDetails> serviceDetails = null;
		List<Schedule> schedules = new LinkedList<>();
		
		serviceDetails = serviceDetailsRepo.finAllByDepartureLocationAndArrivalLocation(depId, arrId);
		for(ServiceDetails service: serviceDetails) {
			schedules.addAll(scheduleRepo.findAllByServiceDetailsAndWeekDay(service.getId(), date.getDay() + 1));
		}
		
		return new ResponseEntity<>(schedules, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/location/{locationName}")
	public ResponseEntity<?> searchLocation(@PathVariable String locationName){
		return new ResponseEntity<>(locationRepo.findAllByLocationNameIgnoreCase("%" + locationName + "%"), HttpStatus.ACCEPTED);
	}
	
}
		