package com.example.BusTicketBookingApi.models.requests;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;

import com.example.BusTicketBookingApi.models.BookedSeat;
import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.Seat;
import com.example.BusTicketBookingApi.models.User;

public class NewBookingRequest {
	
	@Value("${app.current.gst}")
	double gst;
	
	@Value("${app.current.discount}")
	double discount;
	
	
	private int scheduleId;
	private int busId;
	private Date date;
	private Time time;
	private int[] selectedSeats;
	
	public int getScheduleId() {
		return scheduleId;
	}
	
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public int getBusId() {
		return busId;
	}
	
	public void setBusId(int busId) {
		this.busId = busId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Time getTime() {
		return time;
	}
	
	public void setTime(Time time) {
		this.time = time;
	}
	
	public int[] getSelectedSeats() {
		return selectedSeats;
	}
	
	public void setSelectedSeats(int[] selectedSeats) {
		this.selectedSeats = selectedSeats;
	}
	
	
	public BookingDetails getInstanceWithProcessing(User user, Schedule schedule) {
		
		BookingDetails bookingDetails = new BookingDetails();
		bookingDetails.setBasePrice(schedule.getBasePrice());
		bookingDetails.setBookedAt(new Timestamp((new java.util.Date()).getTime()));
		bookingDetails.setUser(user);
		bookingDetails.setDuration(schedule.getDuration());
		bookingDetails.setStatus("HOLD");
		bookingDetails.setSchedule(schedule);
		bookingDetails.setTime(time);
		bookingDetails.setDoj(date);
		bookingDetails.setGst(8.0);
		bookingDetails.setDiscount(5);

		return bookingDetails;
		
	}
	
	public BookedSeat getBookedSeatInstace(BookingDetails bookingDetails, Schedule schedule, Seat seat) {
		BookedSeat bookedSeat = new BookedSeat();
		bookedSeat.setBookingDetails(bookingDetails);
		bookedSeat.setSchedule(schedule);
		bookedSeat.setSeat(seat);
		bookedSeat.setDoj(date);
		bookedSeat.setTime(time);
		bookedSeat.setBusDetails(schedule.getBusDetails());
		
		return bookedSeat;
	}
}
