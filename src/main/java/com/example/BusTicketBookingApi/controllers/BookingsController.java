package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.BookedSeatsRepo;
import com.example.BusTicketBookingApi.daos.BookingDeatilsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.SeatsRepo;
import com.example.BusTicketBookingApi.exceptions.BookingDeatilsNotFound;
import com.example.BusTicketBookingApi.exceptions.ScheduleNotFoundException;
import com.example.BusTicketBookingApi.models.BookedSeat;
import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.Seat;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.models.requests.NewBookingRequest;
import com.example.BusTicketBookingApi.models.responses.BookingDetailsResponse;
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
	
	@GetMapping("/")
	public ResponseEntity<?> getAllBookings(Principal principal){
		Optional<User> user = basicUtil.getUser(principal);
		List<BookingDetails> bookingDetails = bookingDeatilsRepo.findAllByUserId(user.get().getId());
		List<BookingDetailsResponse> response = new LinkedList<>();
		
		BookingDetailsResponse bookingDetailsResponse = new BookingDetailsResponse(); 

		for(BookingDetails bookDetail: bookingDetails) {
			if(!(bookDetail.getStatus().equals("SUCCESS") || bookDetail.getStatus().equals("CANCELED")))
				continue;
			List<BookedSeat> bookedSeats = bookedSeatsRepo.findAllByBookingDetailsId(bookDetail.getId());
			String[] seats = new String[bookedSeats.size()];
			
			for(int i = 0; i < bookedSeats.size(); i++)
				seats[i] = bookedSeats.get(i).getSeat().getSeatName();
			
			response.add(bookingDetailsResponse.getInstance(bookDetail, seats));
		}
		
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/new")
	public ResponseEntity<?> createNewBooking(@RequestBody NewBookingRequest newBookingRequest, 
												Principal principal) throws ScheduleNotFoundException{
		if(newBookingRequest.getSelectedSeats().length == 0)
			return new ResponseEntity<>("{\"msg\": \"Must select one seat atleast\"}", HttpStatus.BAD_REQUEST);
	
		Optional<User> user = basicUtil.getUser(principal);
		
		int seatIds[] = newBookingRequest.getSelectedSeats();
		
		Optional<Schedule> schedule = scheduleRepo.findById(newBookingRequest.getScheduleId());
		
		schedule.orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
		
		if(!(schedule.get().getWeekDay() == newBookingRequest.getDate().getDay() + 1))
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", 
					"Invalid Departure date for this schedule") + "}" , HttpStatus.BAD_REQUEST);
		
		if(!basicUtil.isBothTimesAreEquals(schedule.get().getDepartureTime(), newBookingRequest.getTime()))
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", 
					"Invalid Departure time for this schedule") + "}" , HttpStatus.BAD_REQUEST);
		
		if(newBookingRequest.getTime().getTime() == schedule.get().getDepartureTime().getTime()) {
			BookingDetails bookingDetails = newBookingRequest.getInstanceWithProcessing(user.get(), schedule.get());
			bookingDetails = bookingDeatilsRepo.save(bookingDetails);
			
			for(int id : seatIds) {
				Optional<Seat> seat = seatsRepo.findById(id);
				if(seat.isPresent()) {
					BookedSeat bookedSeat = newBookingRequest.getBookedSeatInstace(bookingDetails, schedule.get(), seat.get());
					bookedSeatsRepo.save(bookedSeat);
				}
			}
			return new ResponseEntity<>("{ \"bookingId\" : " + bookingDetails.getId() +"}", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>("{\"msg\": \"error creating new booking\" }", HttpStatus.BAD_REQUEST);
		
	}
	
	@GetMapping("/confirm/{bookingId}")
	public ResponseEntity<?> getBookingDetails(@PathVariable int bookingId) throws BookingDeatilsNotFound{
		
		Optional<BookingDetails> bookingDetails = bookingDeatilsRepo.findById(bookingId);
		
		bookingDetails.orElseThrow(() -> new BookingDeatilsNotFound("Booking details not found"));
		
		if(bookingDetails.get().getStatus().equalsIgnoreCase("SUCCESS"))
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", 
					"Booking is already confirmed") + "}" , HttpStatus.BAD_REQUEST);
		
		List<BookedSeat> bookedSeats = bookedSeatsRepo.findAllByBookingDetailsId(bookingId);
		String[] seats = new String[bookedSeats.size()];
		
		for(int i = 0; i < bookedSeats.size(); i++)
			seats[i] = bookedSeats.get(i).getSeat().getSeatName();
		
		BookingDetailsResponse bookingDetailsResponse = new BookingDetailsResponse();
		bookingDetailsResponse = bookingDetailsResponse.getInstance(bookingDetails.get(), seats);
		return new ResponseEntity<>(bookingDetailsResponse, HttpStatus.ACCEPTED);
		
	}
	
	
	@PostMapping("/confirm/{bookingId}")
	public ResponseEntity<?> confirmBooking(@PathVariable int bookingId) throws BookingDeatilsNotFound{
		Optional<BookingDetails> bookingDetails = bookingDeatilsRepo.findById(bookingId);
		bookingDetails.orElseThrow(() -> new BookingDeatilsNotFound("Booking details not found"));	
		bookingDetails.get().setBookedAt(new Timestamp((new java.util.Date().getTime())));
		bookingDetails.get().setStatus("SUCCESS");
		bookingDeatilsRepo.save(bookingDetails.get());
		
		List<BookedSeat> bookedSeats = bookedSeatsRepo.findAllByBookingDetailsId(bookingId);
		String[] seats = new String[bookedSeats.size()];
		
		for(int i = 0; i < bookedSeats.size(); i++)
			seats[i] = bookedSeats.get(i).getSeat().getSeatName();
		
		BookingDetailsResponse bookingDetailsResponse = new BookingDetailsResponse();
		
		bookingDetailsResponse = bookingDetailsResponse.getInstance(bookingDetails.get(), seats);
		
		return new ResponseEntity<>(bookingDetailsResponse, HttpStatus.ACCEPTED);
	}
	
	
}
