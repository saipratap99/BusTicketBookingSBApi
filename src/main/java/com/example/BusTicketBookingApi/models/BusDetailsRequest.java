package com.example.BusTicketBookingApi.models;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.BusTicketBookingApi.daos.SeatingTypeRepo;
import com.example.BusTicketBookingApi.utils.BasicUtil;

public class BusDetailsRequest {
	
	@Autowired
	BasicUtil basicUtil;
	
	private String model;
	private String busRegNumber;
	private String busType;
	private int seatCount;
	private String lastMaintance;
	private String onService;
	private int seatingTypeId;
	private String seatingType;
	
	
	public String getSeatingType() {
		return seatingType;
	}

	public void setSeatingType(String seatingType) {
		this.seatingType = seatingType;
	}

	public int getSeatingTypeId() {
		return seatingTypeId;
	}

	public void setSeatingTypeId(int seatingTypeId) {
		this.seatingTypeId = seatingTypeId;
	}

	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public int getSeatCount() {
		return seatCount;
	}
	
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	
	public String getBusRegNumber() {
		return busRegNumber;
	}
	
	public void setBusRegNumber(String busRegNumber) {
		this.busRegNumber = busRegNumber;
	}
	
	public String getBusType() {
		return busType;
	}
	
	public void setBusType(String busType) {
		this.busType = busType;
	}
	/*
	public String getSeatingType() {
		return seatingType;
	}
	
	public void setSeatingType(String seatingType) {
		this.seatingType = seatingType;
	}
	*/
	public String getLastMaintance() {
		return lastMaintance;
	}
	
	public void setLastMaintance(String lastMaintance) {
		this.lastMaintance = lastMaintance;
	}
	
	public String getOnService() {
		return onService;
	}
	
	public void setOnService(String onService) {
		this.onService = onService;
	}
	
	public BusDetails getBusDetailsInstance(SeatingType seatingType) {
		
		
		BusDetails busDetails = new BusDetails();
		busDetails.setModel(model);
		busDetails.setBusRegNumber(busRegNumber);
		busDetails.setBusType(busType);
		busDetails.setSeatCount(seatCount);
		busDetails.setSeatingType(seatingType);
		busDetails.setLastMaintance(Date.valueOf(lastMaintance));
		busDetails.setOnService(Date.valueOf(onService));
		
		return busDetails;
	}

	public BusDetails updateBusDetailsInstance(BusDetails existingBusDetails, SeatingType seatingType) {
		existingBusDetails.setModel(model);
		existingBusDetails.setBusRegNumber(busRegNumber);
		existingBusDetails.setBusType(busType);
		existingBusDetails.setSeatCount(seatCount);
		existingBusDetails.setSeatingType(seatingType);
		existingBusDetails.setLastMaintance(Date.valueOf(lastMaintance));
		existingBusDetails.setOnService(Date.valueOf(onService));
		return existingBusDetails;
	}
	
}
