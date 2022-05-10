package com.example.BusTicketBookingApi.models.requests;

import com.example.BusTicketBookingApi.models.Seat;
import com.example.BusTicketBookingApi.models.SeatingType;

public class NewSeatRequest {
	private int row;
	private int col;
	private String seatName;
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public String getSeatName() {
		return seatName;
	}
	
	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	
	public Seat getSeatInstance(SeatingType seatingType) {
		Seat seat = new Seat();
		
		seat.setRow(row);
		seat.setCol(col);
		seat.setSeatName(seatName);
		seat.setSeatingType(seatingType);
		
		return seat;
	}
}
