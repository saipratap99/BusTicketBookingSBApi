package com.example.BusTicketBookingApi.models;


public class ServiceDetailsRequest {
	
	private String serviceNumber;
	private String serviceType;
	private double distance;
	private String departureLocation;
	private String arrivalLocation;

	public String getServiceNumber() {
		return serviceNumber;
	}
	
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public String getDepartureLocation() {
		return departureLocation;
	}
	
	public void setDepartureLocation(String departureLocation) {
		this.departureLocation = departureLocation;
	}
	
	public String getArrivalLocation() {
		return arrivalLocation;
	}
	
	public void setArrivalLocation(String arrivalLocation) {
		this.arrivalLocation = arrivalLocation;
	}
}
