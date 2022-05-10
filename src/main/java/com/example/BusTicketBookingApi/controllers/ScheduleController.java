package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.exceptions.BusDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.InvalidWeekDayException;
import com.example.BusTicketBookingApi.exceptions.ScheduleDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.ServiceDetailsNotFoundException;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.ServiceDetails;
import com.example.BusTicketBookingApi.models.requests.ScheduleRequest;
import com.example.BusTicketBookingApi.utils.BasicUtil;


@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
	
	@Autowired
	BasicUtil basicUtil;
	
	@Autowired
	ServiceDetailsRepo serviceDetailsRepo;
	
	@Autowired
	BusDetailsRepo busDetailsRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	ScheduleRepo scheduleRepo;

	
	@GetMapping("/")
	public ResponseEntity<?> getSchedules(Principal principal) {
		List<Schedule> schedules = scheduleRepo.findAll();
		List<Map<String, String>> schedulesMap = new LinkedList<>();
		for(Schedule schedule: schedules) {
			Map<String, String> scheduleMap = new LinkedHashMap<>();
			
			scheduleMap.put("id", String.valueOf(schedule.getId()));
			scheduleMap.put("busId", String.valueOf(schedule.getBusDetails().getId()));
			scheduleMap.put("serviceId", String.valueOf(schedule.getServiceDetails().getId()));
			scheduleMap.put("serviceName", String.valueOf(schedule.getServiceDetails().getServiceName()));
			scheduleMap.put("weekDay", String.valueOf(schedule.getWeekDay()));
			scheduleMap.put("departureTime", schedule.getDepartureTime().toString());
			scheduleMap.put("duration", String.valueOf(schedule.getDuration()));
			scheduleMap.put("basePrice", String.valueOf(schedule.getBasePrice()));
			
			schedulesMap.add(scheduleMap);
		}
		
		return new ResponseEntity<>(schedulesMap, HttpStatus.ACCEPTED);
	
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ScheduleRequest scheduleRequest, Principal principal) throws 
									ServiceDetailsNotFoundException, BusDetailsNotFoundException, 
									ParseException, InvalidWeekDayException {
		
		
		Optional<ServiceDetails> serviceDetails = serviceDetailsRepo.findByServiceName(scheduleRequest.getServiceName());
		Optional<BusDetails> busDetails = busDetailsRepo.findById(scheduleRequest.getBusId());
		
		serviceDetails.orElseThrow(() -> new ServiceDetailsNotFoundException("Service details " + scheduleRequest.getServiceName() + " is not found"));
		busDetails.orElseThrow(() -> new BusDetailsNotFoundException("Bus details with id " + scheduleRequest.getBusId() + " is not found"));
		
		if(!String.valueOf(scheduleRequest.getWeekDay()).matches("[1-7]{1}"))
			throw new InvalidWeekDayException("Week day must be valid");
		
		Schedule schedule = scheduleRequest.getScheduleInstance(basicUtil);
		schedule.setBusDetails(busDetails.get());
		schedule.setServiceDetails(serviceDetails.get());
		
		scheduleRepo.save(schedule);
		return new ResponseEntity<Schedule>(schedule, HttpStatus.ACCEPTED);
		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getSchedule(@PathVariable int id,Model model, Principal principal) throws ParseException, ScheduleDetailsNotFoundException {
		Optional<Schedule> schedule = scheduleRepo.findById(id);
		schedule.orElseThrow(() -> new ScheduleDetailsNotFoundException("Schedule details with id " + id + " not found"));
		return new ResponseEntity<Schedule>(schedule.get(), HttpStatus.ACCEPTED);
	}
	
	
	@PutMapping(path = "edit/{id}")
	public ResponseEntity<?> updateSchedule(@PathVariable int id, @RequestBody ScheduleRequest scheduleRequest, 
											Principal principal) throws ScheduleDetailsNotFoundException, 
											ServiceDetailsNotFoundException, BusDetailsNotFoundException, 
											InvalidWeekDayException, ParseException  {
		
		Optional<Schedule> exsitingSchedule = scheduleRepo.findById(id);
		exsitingSchedule.orElseThrow(() -> new ScheduleDetailsNotFoundException("Schedule details with id " + id + " not found"));
		
		Optional<ServiceDetails> serviceDetails = serviceDetailsRepo.findByServiceName(scheduleRequest.getServiceName());
		Optional<BusDetails> busDetails = busDetailsRepo.findById(scheduleRequest.getBusId());
		
		serviceDetails.orElseThrow(() -> new ServiceDetailsNotFoundException("Service details " + scheduleRequest.getServiceName() + " is not found"));
		busDetails.orElseThrow(() -> new BusDetailsNotFoundException("Bus details with id " + scheduleRequest.getBusId() + " is not found"));
		
		if(!String.valueOf(scheduleRequest.getWeekDay()).matches("[1-7]{1}"))
			throw new InvalidWeekDayException("Week day must be valid");
		
		Schedule schedule = scheduleRequest.getUpdatedScheduleInstance(exsitingSchedule.get(),basicUtil);
		schedule.setBusDetails(busDetails.get());
		schedule.setServiceDetails(serviceDetails.get());
		
		scheduleRepo.save(schedule);
		return new ResponseEntity<Schedule>(schedule, HttpStatus.ACCEPTED);
	}
	

}

