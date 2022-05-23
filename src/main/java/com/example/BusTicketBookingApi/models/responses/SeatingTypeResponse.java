package com.example.BusTicketBookingApi.models.responses;

public class SeatingTypeResponse {
	private int id;
	private String seating;
	private int seatCount;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSeating() {
		return seating;
	}
	
	public void setSeating(String seating) {
		this.seating = seating;
	}
	
	public int getSeatCount() {
		return seatCount;
	}
	
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	
}
