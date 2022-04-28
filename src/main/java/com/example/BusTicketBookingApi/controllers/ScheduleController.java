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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.ScheduleRequest;
import com.example.BusTicketBookingApi.models.ServiceDetails;
import com.example.BusTicketBookingApi.models.User;
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
			scheduleMap.put("Service Id", String.valueOf(schedule.getId()));
			scheduleMap.put("Bus Id", String.valueOf(schedule.getBusDetails().getId()));
			scheduleMap.put("Service Id", String.valueOf(schedule.getServiceDetails().getId()));
			scheduleMap.put("Week Day", String.valueOf(schedule.getWeekDay()));
			scheduleMap.put("Departure Time", schedule.getDepartureTime().toString());
			scheduleMap.put("Duration", String.valueOf(schedule.getDuration()));
			scheduleMap.put("Base price", String.valueOf(schedule.getBasePrice()));
			
			schedulesMap.add(scheduleMap);
		}
		
		return new ResponseEntity<>(schedulesMap, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ScheduleRequest scheduleRequest, Principal principal) throws ParseException {
		Optional<User> user = basicUtil.getUser(principal);
		
		Optional<ServiceDetails> serviceDetails = serviceDetailsRepo.findByServiceName(scheduleRequest.getServiceName());
		Optional<BusDetails> busDetails = busDetailsRepo.findById(scheduleRequest.getBusId());
		
		if(user.isPresent() && busDetails.isPresent() && serviceDetails.isPresent() && String.valueOf(scheduleRequest.getWeekDay()).matches("[1-7]{1}")) {
			Schedule schedule = scheduleRequest.getScheduleInstance(basicUtil);
			schedule.setBusDetails(busDetails.get());
			schedule.setServiceDetails(serviceDetails.get());
			
			scheduleRepo.save(schedule);
			return new ResponseEntity<Schedule>(schedule, HttpStatus.ACCEPTED);
		}else
			return new ResponseEntity<String>("Error creating Schedule", HttpStatus.BAD_REQUEST);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getSchedule(@PathVariable int id,Model model, Principal principal) throws ParseException {
		Optional<Schedule> schedule = scheduleRepo.findById(id);
		if(schedule.isPresent())
			return new ResponseEntity<Schedule>(schedule.get(), HttpStatus.ACCEPTED);
		else
			return new ResponseEntity<String>("Couldn't find Schedule with Id " + id, HttpStatus.BAD_REQUEST);
	}
	
	/*
	@PostMapping(path = "/{id}")
	public String updateSchedule(@PathVariable int id, Schedule schedule, String bus, String service, String depTime, String tripDuration, String week, Principal principal, RedirectAttributes redirectAttributes) throws ParseException {
		
		Optional<Schedule> exsitingSchedule = scheduleRepo.findById(id);
		
		if(!exsitingSchedule.isPresent())
			return "redirect:/schedule/";
		
		Optional<Schedule> scheduleOptional = createScheduleObject(schedule, depTime, tripDuration, service, bus, week, principal);
		if(scheduleOptional.isPresent())
			basicUtil.addMsgToRedirectFlash("Schedule " + scheduleOptional.get().getId() + " updated successfully", "success", "show", redirectAttributes);
		
		
		return "redirect:/schedule/";
	}
	
	*/
}

