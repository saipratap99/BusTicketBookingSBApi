package com.example.BusTicketBookingApi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
	
	@GetMapping("")
	public ResponseEntity<?> index(){
		return new ResponseEntity<String>("Welcome to Bus Ticket Booking", HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/offers")
	public ResponseEntity<?> getOffers(){
		return new ResponseEntity<String>("No offers currently!", HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/about")
	public ResponseEntity<?> about(){
		return new ResponseEntity<String>("{\"data\": \"You can book tickets online all over the India.\"}", HttpStatus.ACCEPTED);
	}
}
