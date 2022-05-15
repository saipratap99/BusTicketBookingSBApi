package com.example.BusTicketBookingApi.controllers;

public class IncorrectOTPException extends Exception {

	public IncorrectOTPException(String message) {
		super(message);
	}
	
}
