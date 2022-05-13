package com.example.BusTicketBookingApi.exceptions;

public class ExpiredSessionException extends Exception{

	public ExpiredSessionException(String message) {
		super(message);
	}
	
}
