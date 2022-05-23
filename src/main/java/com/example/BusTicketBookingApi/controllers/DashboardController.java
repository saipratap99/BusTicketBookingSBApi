package com.example.BusTicketBookingApi.controllers;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusTicketBookingApi.daos.BookingDeatilsRepo;
import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.exceptions.UserNotFoundException;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
	@Autowired
	BusDetailsRepo busDetailsRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	@Autowired
	BookingDeatilsRepo bookingDeatilsRepo;
	
	@Autowired
	ServiceDetailsRepo serviceDetailsRepo;
	
	@Autowired
	ScheduleRepo scheduleRepo;
	
	@Autowired
	UserRepo userRepo;
	
	
	@GetMapping("/get-operator-name")
	public ResponseEntity<?> getOperatorName(Principal principal) throws UserNotFoundException{
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		return new ResponseEntity<>("{" + basicUtil.getJSONString("operatorName", user.get().getOperator()) + "}", HttpStatus.OK);
	}
	
	
	@GetMapping("/buses")
	public ResponseEntity<?> getBusesStats(Principal principal) throws UserNotFoundException{
		
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> busesStat = new HashMap<>();
		if(basicUtil.isAdmin(user.get())) {	
			busesStat.put("totalCount", busDetailsRepo.getAllBuses());
			busesStat.put("runningCount", busDetailsRepo.getAllRunningBuses());
			busesStat.put("stoppedCount", busDetailsRepo.getAllStoppedBuses());
			return new ResponseEntity<>(busesStat, HttpStatus.OK);
		}else {
			busesStat.put("totalCount", busDetailsRepo.getAllBusesOfOperator(user.get().getOperator()));
			busesStat.put("runningCount", busDetailsRepo.getAllRunningBusesOfOperator(user.get().getOperator()));
			busesStat.put("stoppedCount", busDetailsRepo.getAllStoppedBusesOfOperator(user.get().getOperator()));
			return new ResponseEntity<>(busesStat, HttpStatus.OK);
		}
	}
	
	@GetMapping("/bookings")
	public ResponseEntity<?> getBookingStats(Principal principal) throws UserNotFoundException{
		
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> bookingsStat = new HashMap<>();
		if(basicUtil.isAdmin(user.get())) {	
			bookingsStat.put("totalCount", bookingDeatilsRepo.countAllBookings());
			bookingsStat.put("successCount", bookingDeatilsRepo.countAllBookingsForStatus("SUCCESS"));
			bookingsStat.put("cancelledCount", bookingDeatilsRepo.countAllBookingsForStatus("CANCELED"));
			return new ResponseEntity<>(bookingsStat, HttpStatus.OK);
		}else {
			bookingsStat.put("totalCount", bookingDeatilsRepo.countAllBookingsOfOperator(user.get().getOperator()));
			bookingsStat.put("successCount", bookingDeatilsRepo.countAllBookingsOfOperatorForStatus(user.get().getOperator(), "SUCCESS"));
			bookingsStat.put("cancelledCount", bookingDeatilsRepo.countAllBookingsOfOperatorForStatus(user.get().getOperator(), "CANCELED"));
			return new ResponseEntity<>(bookingsStat, HttpStatus.OK);
		}
	}
	
	@GetMapping("/services")
	public ResponseEntity<?> getServiceStats(Principal principal) throws UserNotFoundException{
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> servicesStat = new HashMap<>();
		servicesStat.put("totalCount", serviceDetailsRepo.countAllServices());
		servicesStat.put("runningCount", serviceDetailsRepo.countAllRunningServices());
		servicesStat.put("stoppedCount", serviceDetailsRepo.countAllStoppedServices());
		
		return new ResponseEntity<>(servicesStat, HttpStatus.OK);
	}
	
	
	@GetMapping("/schedules")
	public ResponseEntity<?> getScheduleStats(Principal principal) throws UserNotFoundException{
		
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> schedulesStats = new HashMap<>();
		if(basicUtil.isAdmin(user.get())) {	
			schedulesStats.put("totalCount", scheduleRepo.countAllSchedules());
			schedulesStats.put("runningCount", scheduleRepo.countAllRunningSchedules());
			schedulesStats.put("stoppedCount", scheduleRepo.countAllStoppedSchedules());
			return new ResponseEntity<>(schedulesStats, HttpStatus.OK);
		}else {
			schedulesStats.put("totalCount", scheduleRepo.countAllSchedulesOfOperator(user.get().getOperator()));
			schedulesStats.put("runningCount", scheduleRepo.countAllRunningSchedulesOfOperator(user.get().getOperator()));
			schedulesStats.put("stoppedCount", scheduleRepo.countAllStoppedSchedulesOfOperator(user.get().getOperator()));
			return new ResponseEntity<>(schedulesStats, HttpStatus.OK);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> getUserStats(Principal principal){
		Map<String, Integer> userStats = new HashMap<>();
		
		userStats.put("totalCount", userRepo.countAllUsers());
		userStats.put("customersCount", userRepo.countAllCustomers());
		userStats.put("operatorsCount", userRepo.countAllOperators());
		return new ResponseEntity<>(userStats, HttpStatus.OK);
	}
	
}
