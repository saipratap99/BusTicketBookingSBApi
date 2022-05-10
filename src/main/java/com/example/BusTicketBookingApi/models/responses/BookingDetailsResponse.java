package com.example.BusTicketBookingApi.models.responses;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;

public class BookingDetailsResponse {
	private int id;
	private BusDetails busDetails;
	private Schedule scheduleDetails;
	private String[] seats;
	private Date doj;
	private Time time;
	private String status;
	private double basePrice;
	private double gst;
	private double discount;
	private Timestamp bookedAt;
	
	
	public Timestamp getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(Timestamp bookedAt) {
		this.bookedAt = bookedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BusDetails getBusDetails() {
		return busDetails;
	}
	
	public void setBusDetails(BusDetails busDetails) {
		this.busDetails = busDetails;
	}
	
	public Schedule getScheduleDetails() {
		return scheduleDetails;
	}
	
	public void setScheduleDetails(Schedule scheduleDetails) {
		this.scheduleDetails = scheduleDetails;
	}
	
	public String[] getSeats() {
		return seats;
	}
	
	public void setSeats(String[] seats) {
		this.seats = seats;
	}
	
	public Date getDoj() {
		return doj;
	}
	
	public void setDoj(Date doj) {
		this.doj = doj;
	}
	
	public Time getTime() {
		return time;
	}
	
	public void setTime(Time time) {
		this.time = time;
	}
	
	public double getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	public double getGst() {
		return gst;
	}
	
	public void setGst(double gst) {
		this.gst = gst;
	}
	
	public double getDiscount() {
		return discount;
	}
	
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
	public BookingDetailsResponse getInstance(BookingDetails bookingDetails, String[] seats) {
		
		BookingDetailsResponse bookingDetailsResponse = new BookingDetailsResponse();
		
		bookingDetailsResponse.setId(bookingDetails.getId());
		bookingDetailsResponse.setGst(bookingDetails.getGst());		
		bookingDetailsResponse.setDiscount(bookingDetails.getDiscount());
		bookingDetailsResponse.setBasePrice(bookingDetails.getBasePrice());
		bookingDetailsResponse.setBusDetails(bookingDetails.getSchedule().getBusDetails());
		bookingDetailsResponse.setScheduleDetails(bookingDetails.getSchedule());
		bookingDetailsResponse.setDoj(bookingDetails.getDoj());
		bookingDetailsResponse.setTime(bookingDetails.getTime());
		bookingDetailsResponse.setSeats(seats);
		bookingDetailsResponse.setStatus(bookingDetails.getStatus());
		bookingDetailsResponse.setBookedAt(bookingDetails.getBookedAt());
		
		return bookingDetailsResponse;
	}
	
	
	
}
