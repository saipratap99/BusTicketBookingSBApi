package com.example.BusTicketBookingApi.controllers;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.SeatingTypeRepo;
import com.example.BusTicketBookingApi.daos.SeatsRepo;
import com.example.BusTicketBookingApi.models.SeatingType;
import com.example.BusTicketBookingApi.models.responses.SeatingTypeResponse;

@RestController
@RequestMapping("/api/v1/seating-types")
public class SeatingTypesController {
	
	@Autowired
	SeatingTypeRepo seatingTypeRepo;
	
	@Autowired 
	SeatsRepo seatsRepo;
	
	@GetMapping("/")
	public ResponseEntity<?> getAllSeatingTypes(){
		List<SeatingType> seatingTypes = seatingTypeRepo.findAll();
		List<SeatingTypeResponse> seatings = new LinkedList<>();
		for(SeatingType seating: seatingTypes) {
			SeatingTypeResponse seatingType = new SeatingTypeResponse();
			seatingType.setId(seating.getId());
			seatingType.setSeating(seating.getSeating());
			seatingType.setSeatCount(seatsRepo.countNumberOfSeatsForSeatingType(seating.getSeating()));
			
			seatings.add(seatingType);
		}
		
		return new ResponseEntity<>(seatings, HttpStatus.OK);
	}
}
