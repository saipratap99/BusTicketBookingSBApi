package com.example.BusTicketBookingApi.models.responses;

public class SeatResponse {

	private int seatId;
	private int seatRow;
	private int seatCol;
	private String seatName;
	private String seatingType;
	private boolean isBooked;
	
	public SeatResponse() {
	}
	
	

	public String getSeatingType() {
		return seatingType;
	}



	public void setSeatingType(String seatingType) {
		this.seatingType = seatingType;
	}



	public SeatResponse(int seatId, int seatRow, int seatCol, String seatName, String seatingType, boolean isBooked) {
		this.seatId = seatId;
		this.seatRow = seatRow;
		this.seatCol = seatCol;
		this.seatName = seatName;
		this.seatingType = seatingType;
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
