package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.BookedSeatsRepo;
import com.example.BusTicketBookingApi.daos.BookingDeatilsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.SeatsRepo;
import com.example.BusTicketBookingApi.models.BookedSeat;
import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.NewBookingRequest;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.Seat;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingsController {
	
	@Autowired
	BasicUtil basicUtil;
	
	@Autowired
	ScheduleRepo scheduleRepo;
	
	@Autowired
	BookingDeatilsRepo bookingDeatilsRepo;
	
	@Autowired 
	BookedSeatsRepo bookedSeatsRepo;
	
	@Autowired
	SeatsRepo seatsRepo;
	
	@PostMapping("/new")
	public ResponseEntity<?> createNewBooking(@RequestBody NewBookingRequest newBookingRequest, Principal principal){
		Optional<User> user = basicUtil.getUser(principal);
		
		int seatIds[] = newBookingRequest.getSelectedSeats();
		
		Optional<Schedule> schedule = scheduleRepo.findById(newBookingRequest.getScheduleId());
		
		if(schedule.isPresent() && user.isPresent()) {
		    System.out.println("sch use");
			if(newBookingRequest.getTime().getTime() == schedule.get().getDepartureTime().getTime()) {
				System.out.println("date time");
				BookingDetails bookingDetails = newBookingRequest.getInstanceWithProcessing(user.get(), schedule.get());
				bookingDetails = bookingDeatilsRepo.save(bookingDetails);
				
				for(int id : seatIds) {
					Optional<Seat> seat = seatsRepo.findById(id);
					if(seat.isPresent()) {
						BookedSeat bookedSeat = newBookingRequest.getBookedSeatInstace(bookingDetails, schedule.get(), seat.get());
						bookedSeatsRepo.save(bookedSeat);
					}
				}
				
				return new ResponseEntity<>("\"bookingId\": " + bookingDetails.getId(), HttpStatus.ACCEPTED);
			}
		}
		return new ResponseEntity<>("\"msg\": error creating new booking", HttpStatus.BAD_REQUEST);
		
		
	}
	
	
}
