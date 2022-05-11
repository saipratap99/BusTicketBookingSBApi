package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.SeatingTypeRepo;
import com.example.BusTicketBookingApi.daos.SeatsRepo;
import com.example.BusTicketBookingApi.exceptions.BusDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.ScheduleNotFoundException;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.Seat;
import com.example.BusTicketBookingApi.models.SeatingType;
import com.example.BusTicketBookingApi.models.requests.NewSeatRequest;
import com.example.BusTicketBookingApi.models.requests.NewSeatingLayoutRequest;
import com.example.BusTicketBookingApi.models.responses.SeatResponse;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatsController {
	
	@Autowired
	ScheduleRepo scheduleRepo;
	
	@Autowired
	BusDetailsRepo busDetailsRepo;
	
	@Autowired
	SeatsRepo seatsRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	@Autowired
	SeatingTypeRepo seatingTypeRepo;
	
	@Autowired
	BookedSeatsRepo bookedSeatsRepo;
	
	@GetMapping("/schedule/{scheduleId}/bus/{busId}/{doj}/{depTime}")
	public ResponseEntity<?> getAvailableSeats(@PathVariable int scheduleId, @PathVariable int busId,
											   @PathVariable Date doj, @PathVariable String depTime, Principal principal) 
											   throws BusDetailsNotFoundException, ScheduleNotFoundException{
		
		Time time = Time.valueOf(depTime.replaceAll("-", ":"));
		
		Optional<Schedule> schedule = scheduleRepo.findById(scheduleId);
		Optional<BusDetails> busDetails = busDetailsRepo.findById(busId);
		
		
		schedule.orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
		busDetails.orElseThrow(() -> new BusDetailsNotFoundException("Bus details not found!"));
		
		if(!(schedule.get().getWeekDay() == doj.getDay() + 1))
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Invalid Departure date for this schedule") + "}" , HttpStatus.BAD_REQUEST);
		
		if(!basicUtil.isBothTimesAreEquals(schedule.get().getDepartureTime(), time))
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Invalid Departure time for this schedule") + "}" , HttpStatus.BAD_REQUEST);
		
		if(schedule.get().getWeekDay() == doj.getDay() + 1 && basicUtil.isBothTimesAreEquals(schedule.get().getDepartureTime(), time)) {
			
			List<Seat> totalSeats = seatsRepo.findAllBySeatingType(busDetails.get().getSeatingType().getId());
			List<Seat> bookedSeats = bookedSeatsRepo.findAllByTripDetails(scheduleId, busId, doj.toString(), time.toString());
			
			Map<Integer, SeatResponse> availableSeats = new LinkedHashMap<>();

			for(Seat seat: totalSeats)
				availableSeats.put(seat.getId(), new SeatResponse(seat.getId(), seat.getRow(), seat.getCol(), seat.getSeatName(), seat.getSeatingType().getSeating(), false));
			
			for(Seat seat: bookedSeats)
				if(availableSeats.containsKey(seat.getId()))
					availableSeats.get(seat.getId()).setBooked(true);
			
			
			return new ResponseEntity<>(availableSeats.values(), HttpStatus.ACCEPTED);
		}
	
		return new ResponseEntity<String>("{\"msg\": \"Error fetching seats\"}" , HttpStatus.BAD_REQUEST);
		
	}
	
	@PostMapping("/new")
	public ResponseEntity<?> createNewSeatingLayout(@RequestBody NewSeatingLayoutRequest newSeatingLayoutRequest){

		SeatingType seatingType = newSeatingLayoutRequest.getSeatingTypeInstance();
		seatingTypeRepo.save(seatingType);
		
		for(NewSeatRequest seat: newSeatingLayoutRequest.getSeats()) {
			Seat seatInst = seat.getSeatInstance(seatingType);
			if(!(seatInst.getSeatName().isEmpty()))
				seatsRepo.save(seatInst);
		}
		
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Layout added for seating " + newSeatingLayoutRequest.getSeatingType()) + "}" , HttpStatus.OK);
		
	}
}
