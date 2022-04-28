package com.example.BusTicketBookingApi.models;

import java.sql.Date;
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
@Table(name = "bus_details")
public class BusDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "model", nullable = false)
	private String model;
	
	@Column(name = "bus_reg_number", nullable = false)
	private String busRegNumber;
	
	@Column(name = "bus_type", nullable = false)
	private String busType;
	
	@Column(name = "seating_type", nullable = false)
	private String seatingType;

	@Column(name = "seat_count", nullable = false)
	private int seatCount;

	@Column(name = "last_maintance", nullable = false)
	private Date lastMaintance;
	
	@Column(name = "on_service", nullable = false)
	private Date onService;
	
	@Column(name = "bus_name", nullable = false)
	private String busName;
	
	@ManyToOne(optional = false)
	private User operator;
	
//	@OneToMany(mappedBy = "busDetails")
//	List<Schedule> schedule;
	
	
	// created_at, updated_at
	
	public void generateBusName() {
		this.setBusName(this.operator.getOperator() + " " + this.getModel() + " " + this.getSeatingType() + " " + this.getBusType());
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
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

	public String getSeatingType() {
		return seatingType;
	}

	public void setSeatingType(String seatingType) {
		this.seatingType = seatingType;
	}

	public Date getLastMaintance() {
		return lastMaintance;
	}

	public void setLastMaintance(Date lastMaintance) {
		this.lastMaintance = lastMaintance;
	}

	public Date getOnService() {
		return onService;
	}

	public void setOnService(Date onService) {
		this.onService = onService;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}
/*
	public List<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}
*/

	
}


