package com.example.BusTicketBookingApi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<?> expiredJWTTokenException(ExceptionHandler exception){
		System.out.println("Expired Token");
		return new ResponseEntity<String>("Token expired", HttpStatus.UNAUTHORIZED);	
	}
	
}
