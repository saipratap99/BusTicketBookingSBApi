package com.example.BusTicketBookingApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationsController {
	
	@Autowired
	LocationRepo locationRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	@GetMapping("/")
	public ResponseEntity<?> getAllLocations(){
		return new ResponseEntity<>(locationRepo.findAll(), HttpStatus.ACCEPTED);
	}
}
