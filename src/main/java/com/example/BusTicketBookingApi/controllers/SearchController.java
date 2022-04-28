package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.models.Location;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.ServiceDetails;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
		
	@Autowired
	LocationRepo locationRepo;
	
	@Autowired
	ServiceDetailsRepo serviceDetailsRepo;

	@Autowired
	ScheduleRepo scheduleRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	/*
	@GetMapping("/get")
	public String getBuses(String depLocation, String arrLocation, Date date,Model model, RedirectAttributes redirectAttributes, Principal principal) {
		
		List<String> locations = locationRepo.findAllProjectedByLocationName();
		
		model.addAttribute("locations", locations);
		model.addAttribute("arrLocation", arrLocation);
		model.addAttribute("depLocation", depLocation);
		model.addAttribute("date", date);
		
		
		Optional<Location> departureLocation = locationRepo.findByLocationName(depLocation);
		Optional<Location> arrivalLocation = locationRepo.findByLocationName(arrLocation);
		
		if(departureLocation.isPresent() && arrivalLocation.isPresent()) {
			if(departureLocation.get().getId() != arrivalLocation.get().getId()) {
				return "redirect:/bookings/get/" + depLocation+ "/" + departureLocation.get().getId() + "/" + arrLocation + "/" + arrivalLocation.get().getId() + "/" + date.toString();
			}
		}
		
		basicUtil.addNavBarAttributesToModel(principal, model);
		
		return "redirect:/bookings/new.jsp";
	}
	*/
	
	@GetMapping("/buses/{depLocation}/{depId}/{arrLocation}/{arrId}/{date}")
	public ResponseEntity<?> availableBuses(@PathVariable String depLocation,@PathVariable int depId,@PathVariable String arrLocation,@PathVariable int arrId,@PathVariable Date date, Model model, Principal principal) {
		
		List<ServiceDetails> serviceDetails = null;
		List<Schedule> schedules = new LinkedList<>();
		
		serviceDetails = serviceDetailsRepo.finAllByDepartureLocationAndArrivalLocation(depId, arrId);
		for(ServiceDetails service: serviceDetails) {
			schedules.addAll(scheduleRepo.findAllByServiceDetailsAndWeekDay(service.getId(), date.getDay() + 1));
		}
		return new ResponseEntity<>(schedules, HttpStatus.ACCEPTED);
	}
	
}
		