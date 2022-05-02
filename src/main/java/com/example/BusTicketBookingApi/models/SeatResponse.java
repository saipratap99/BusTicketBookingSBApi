package com.example.BusTicketBookingApi.models;

public class SeatResponse {

	private int seatId;
	private int seatRow;
	private int seatCol;
	private String seatName;
	private boolean isBooked;
	
	public SeatResponse() {
	}

	public SeatResponse(int seatId, int seatRow, int seatCol, String seatName, boolean isBooked) {
		this.seatId = seatId;
		this.seatRow = seatRow;
		this.seatCol = seatCol;
		this.seatName = seatName;
		this.isBooked = isBooked;
	}

	public int getSeatId() {
		return seatId;
	}
	
	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	public int getSeatRow() {
		return seatRow;
	}
	
	public void setSeatRow(int seatRow) {
		this.seatRow = seatRow;
	}
	
	public int getSeatCol() {
		return seatCol;
	}
	
	public void setSeatCol(int seatCol) {
		this.seatCol = seatCol;
	}
	
	public String getSeatName() {
		return seatName;
	}
	
	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	
	public boolean isBooked() {
		return isBooked;
	}
	
	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
	
	
}
