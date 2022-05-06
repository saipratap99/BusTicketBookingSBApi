package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.BookedSeatsRepo;
import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.SeatsRepo;
import com.example.BusTicketBookingApi.exceptions.BusDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.ScheduleNotFoundException;
import com.example.BusTicketBookingApi.models.BookedSeat;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.Seat;
import com.example.BusTicketBookingApi.models.SeatResponse;
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
	BookedSeatsRepo bookedSeatsRepo;
	
	@GetMapping("/schedule/{scheduleId}/bus/{busId}/{doj}/{time}")
	public ResponseEntity<?> getAvailableSeats(@PathVariable int scheduleId, @PathVariable int busId,@PathVariable Date doj,@PathVariable Time time, Principal principal){
		
		try {
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
		}catch(ScheduleNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST); 
		} catch (BusDetailsNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>("{\"msg\": \"Error fetching seats\"}" , HttpStatus.BAD_REQUEST);
		
	}
}
