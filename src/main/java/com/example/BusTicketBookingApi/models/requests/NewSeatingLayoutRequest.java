package com.example.BusTicketBookingApi.models.requests;

import java.util.List;

import com.example.BusTicketBookingApi.models.SeatingType;

public class NewSeatingLayoutRequest {
	private String seatingType;
	private List<NewSeatRequest> seats;
	
	public String getSeatingType() {
		return seatingType;
	}
	
	public void setSeatingType(String seatingType) {
		this.seatingType = seatingType;
	}
	
	public List<NewSeatRequest> getSeats() {
		return seats;
	}
	
	public void setSeats(List<NewSeatRequest> seats) {
		this.seats = seats;
	}
	
	
	public SeatingType getSeatingTypeInstance() {
		SeatingType seatingType = new SeatingType();
		seatingType.setSeating(this.seatingType);
		return seatingType;
	}
}
