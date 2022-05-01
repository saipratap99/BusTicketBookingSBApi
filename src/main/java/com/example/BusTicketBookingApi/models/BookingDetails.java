package com.example.BusTicketBookingApi.models;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "booking_details")
public class BookingDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String status;
	
	@Column(name = "base_price", nullable = false, columnDefinition = "Decimal(10,2) default '0.0'")
	private double basePrice;
	
	@Column(name = "gst", nullable = false, columnDefinition = "Decimal(10,2) default '0.0'")
	private double gst;
	
	@Column(columnDefinition = "Decimal(10,2) default '0.0'")
	private double discount;
	
	@Column(name = "booked_at", nullable = false)
	private Timestamp bookedAt;
	
	@Column(nullable = false)
	private Date doj;
	
	@Column(nullable = false)
	private Time time;
	
	@Column(nullable = false)
	private int duration;
	
	
	@Column(columnDefinition = "Decimal(10,2) default '0.0'")
	private double ratings;
		
	@ManyToOne(optional = false) // many bookings belongs to one user
	private User user; 
	
	@ManyToOne(optional = false)
	private Schedule schedule;

	// created_at, updated_at
	
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

	public Timestamp getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(Timestamp bookedAt) {
		this.bookedAt = bookedAt;
	}

	public double getRatings() {
		return ratings;
	}

	public void setRatings(double ratings) {
		this.ratings = ratings;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	
	
    
}

