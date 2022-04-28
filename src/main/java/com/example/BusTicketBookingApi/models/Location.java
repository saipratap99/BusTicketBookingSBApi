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
@Table(name = "locations")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "location_name", nullable = false)
	private String locationName;

	@ManyToOne(optional = false)
	private State state;
	
//	@OneToMany(mappedBy = "departureLocation")
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private List<ServiceDetails> departureServiceDetails;
	
//	@OneToMany(mappedBy = "arrivalLocation")
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private List<ServiceDetails> arrivalServiceDetails;
	

	// created_at, updated_at
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
/*
	public List<ServiceDetails> getDepartureServiceDetails() {
		return departureServiceDetails;
	}

	public void setDepartureServiceDetails(List<ServiceDetails> departureServiceDetails) {
		this.departureServiceDetails = departureServiceDetails;
	}

	public List<ServiceDetails> getArrivalServiceDetails() {
		return arrivalServiceDetails;
	}

	public void setArrivalServiceDetails(List<ServiceDetails> arrivalServiceDetails) {
		this.arrivalServiceDetails = arrivalServiceDetails;
	}
	*/
	

}

