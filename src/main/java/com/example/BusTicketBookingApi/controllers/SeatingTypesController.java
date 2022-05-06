package com.example.BusTicketBookingApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.SeatingTypeRepo;

@RestController
@RequestMapping("/api/v1/seating-types")
public class SeatingTypesController {
	
	@Autowired
	SeatingTypeRepo seatingTypeRepo;
	
	@GetMapping("/")
	public ResponseEntity<?> getAllSeatingTypes(){
		return new ResponseEntity<>(seatingTypeRepo.findAll(), HttpStatus.OK);
	}
}
