package com.example.BusTicketBookingApi.models;

import java.sql.Date;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.BusTicketBookingApi.utils.BasicUtil;

public class ScheduleRequest {
	
	private int busId;
	private String serviceName;
	private double basePrice;
	private String departureTime;
	private String duration;
	private int weekDay;
	private String departureDate;
	private String arrivalDate;
	
	
	public double getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	public int getWeekDay() {
		return weekDay;
	}
	
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	
	public String getDepartureDate() {
		return departureDate;
	}
	
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	
	public String getArrivalDate() {
		return arrivalDate;
	}
	
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	
	public String getDepartureTime() {
		return departureTime;
	}
	
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getBusId() {
		return busId;
	}

	public void setBusId(int busId) {
		this.busId = busId;
	}

	public Schedule getScheduleInstance(BasicUtil basicUtil) throws ParseException {
		Schedule schedule =  new Schedule();
		
		schedule.setBasePrice(basePrice);
		schedule.setWeekDay(weekDay);
		schedule.setDuration(Integer.parseInt(duration.split(":")[0])*60 + Integer.parseInt(duration.split(":")[1]));
		schedule.setDepartureTime(basicUtil.parseStringToSqlTime(departureTime));
		schedule.setDepartureDate(departureDate != null ? Date.valueOf(departureDate) : null);
		schedule.setArrivalDate(arrivalDate != null ? Date.valueOf(arrivalDate) : null);
		
		return schedule;
	}

	public Schedule getUpdatedScheduleInstance(Schedule schedule, BasicUtil basicUtil) throws ParseException {

		schedule.setBasePrice(basePrice);
		schedule.setWeekDay(weekDay);
		schedule.setDuration(Integer.parseInt(duration.split(":")[0])*60 + Integer.parseInt(duration.split(":")[1]));
		schedule.setDepartureTime(basicUtil.parseStringToSqlTime(departureTime));
		schedule.setDepartureDate(departureDate != null ? Date.valueOf(departureDate) : null);
		schedule.setArrivalDate(arrivalDate != null ? Date.valueOf(arrivalDate) : null);
		
		return schedule;
	}
}
