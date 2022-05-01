package com.example.BusTicketBookingApi.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="seat_row", nullable = false)
	private int seatRow;
	
	@Column(name = "seat_col", nullable = false)
	private int seatCol;
	
	@Column(name="seat_name", nullable = false)
	private String seatName;
	
	@Column(name = "seat_descripton", nullable = true)
	private String seatDescripton;
	
	@ManyToOne(optional = false)
	private SeatingType seatingType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRow() {
		return seatRow;
	}

	public void setRow(int row) {
		this.seatRow = row;
	}

	public int getCol() {
		return seatCol;
	}

	public void setCol(int col) {
		this.seatCol = col;
	}

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public String getSeatDescripton() {
		return seatDescripton;
	}

	public void setSeatDescripton(String seatDescripton) {
		this.seatDescripton = seatDescripton;
	}

	public SeatingType getSeatingType() {
		return seatingType;
	}

	public void setSeatingType(SeatingType seatingType) {
		this.seatingType = seatingType;
	}
	
}
