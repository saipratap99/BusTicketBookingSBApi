package com.example.BusTicketBookingApi.models;

import java.sql.Date;
import java.sql.Time;

public class BookingDetailsResponse {
	private BusDetails busDetails;
	private Schedule scheduleDetails;
	private String[] seats;
	private Date doj;
	private Time time;
	private double basePrice;
	private double gst;
	private double discount;
	
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
		bookingDetailsResponse.setGst(bookingDetails.getGst());
		bookingDetailsResponse.setDiscount(bookingDetails.getDiscount());
		bookingDetailsResponse.setBasePrice(bookingDetails.getBasePrice());
		bookingDetailsResponse.setBusDetails(bookingDetails.getSchedule().getBusDetails());
		bookingDetailsResponse.setScheduleDetails(bookingDetails.getSchedule());
		bookingDetailsResponse.setDoj(bookingDetails.getDoj());
		bookingDetailsResponse.setTime(bookingDetails.getTime());
		bookingDetailsResponse.setSeats(seats);
		
		return bookingDetailsResponse;
	}
	
	
	
}
