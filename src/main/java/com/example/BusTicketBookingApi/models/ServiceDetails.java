package com.example.BusTicketBookingApi.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "service_details")
public class ServiceDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "service_name", nullable = false)
	private String serviceName;
	
	@Column(name = "service_number", nullable = false)
	private String serviceNumber;
	
	@Column(name = "service_type", nullable = false)
	private String serviceType;
		
	@Column(nullable = false)
	private double distance;
	
	@Column(name = "is_deleted", columnDefinition = "boolean default false")
	private boolean isDeleted;

	@ManyToOne(optional = false) // many services may have same departure location.
	private Location departureLocation;
	
	@ManyToOne(optional = false) // many services may have same arrival location. 
	private Location arrivalLocation;

//	@OneToMany(mappedBy = "serviceDetails")
//	private List<Schedule> schedule;
	// driver, conductor, created_at, updated_at

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

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

	public Location getDepartureLocation() {
		return departureLocation;
	}

	public void setDepartureLocation(Location departureLocation) {
		this.departureLocation = departureLocation;
	}

	public Location getArrivalLocation() {
		return arrivalLocation;
	}

	public void setArrivalLocation(Location arrivalLocation) {
		this.arrivalLocation = arrivalLocation;
	}
/*
	public List<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}
*/
	
	public void genrateServiceName() { 
		this.setServiceName(this.getServiceNumber() + " " + this.getDepartureLocation().getLocationName() + " - " + this.getArrivalLocation().getLocationName() + " " + this.getServiceType());
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "ServiceDetails [id=" + id + ", serviceName=" + serviceName + ", serviceNumber=" + serviceNumber
				+ ", serviceType=" + serviceType + ", distance=" + distance + ", departureLocation=" + departureLocation
				+ ", arrivalLocation=" + arrivalLocation + ", schedule=" + "schedule" + "]";
	}

	
}

