package com.example.BusTicketBookingApi.controllers;

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
		Integer totalCount, runningCount, stoppedCount;
		
		if(basicUtil.isAdmin(user.get())) {	
			totalCount = busDetailsRepo.getAllBuses();
			runningCount = busDetailsRepo.getAllRunningBuses();
			stoppedCount = busDetailsRepo.getAllStoppedBuses();
		}else {
			totalCount = busDetailsRepo.getAllBusesOfOperator(user.get().getOperator());
			runningCount = busDetailsRepo.getAllRunningBusesOfOperator(user.get().getOperator());
			stoppedCount = busDetailsRepo.getAllStoppedBusesOfOperator(user.get().getOperator());
		}
		
		busesStat.put("totalCount", totalCount != null ? totalCount : 0);
		busesStat.put("runningCount", runningCount != null ? runningCount : 0);
		busesStat.put("stoppedCount", stoppedCount != null ? stoppedCount : 0);
		return new ResponseEntity<>(busesStat, HttpStatus.OK);
		
	}
	
	@GetMapping("/bookings")
	public ResponseEntity<?> getBookingStats(Principal principal) throws UserNotFoundException{
		
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> bookingsStat = new HashMap<>();
		Integer totalCount, successCount, cancelledCount;
		
		if(basicUtil.isAdmin(user.get())) {	
			totalCount = bookingDeatilsRepo.countAllBookings();
			successCount = bookingDeatilsRepo.countAllBookingsForStatus("SUCCESS");
			cancelledCount = bookingDeatilsRepo.countAllBookingsForStatus("CANCELED");
			
		}else {
			totalCount =  bookingDeatilsRepo.countAllBookingsOfOperator(user.get().getOperator());
			successCount = bookingDeatilsRepo.countAllBookingsOfOperatorForStatus(user.get().getOperator(), "SUCCESS");
			cancelledCount = bookingDeatilsRepo.countAllBookingsOfOperatorForStatus(user.get().getOperator(), "CANCELED");
		}
		
		bookingsStat.put("totalCount", totalCount != null ? totalCount : 0);
		bookingsStat.put("successCount", successCount != null ? successCount : 0);
		bookingsStat.put("cancelledCount", cancelledCount != null ? cancelledCount : 0);
		
		
		return new ResponseEntity<>(bookingsStat, HttpStatus.OK);
	}
	
	@GetMapping("/services")
	public ResponseEntity<?> getServiceStats(Principal principal) throws UserNotFoundException{
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> servicesStat = new HashMap<>();
		Integer totalCount, runningCount, stoppedCount;
		
		totalCount = serviceDetailsRepo.countAllServices();
		runningCount = serviceDetailsRepo.countAllRunningServices();
		stoppedCount = serviceDetailsRepo.countAllStoppedServices();
		
		servicesStat.put("totalCount", totalCount != null ? totalCount : 0);
		servicesStat.put("runningCount", runningCount != null ? runningCount : 0);
		servicesStat.put("stoppedCount", stoppedCount != null ? stoppedCount : 0);
		
		return new ResponseEntity<>(servicesStat, HttpStatus.OK);
	}
	
	
	@GetMapping("/schedules")
	public ResponseEntity<?> getScheduleStats(Principal principal) throws UserNotFoundException{
		
		Optional<User> user = basicUtil.getUser(principal);
		user.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		Map<String, Integer> schedulesStats = new HashMap<>();
		Integer totalCount, runningCount, stoppedCount;
		
		if(basicUtil.isAdmin(user.get())) {	
			totalCount = scheduleRepo.countAllSchedules();
			runningCount = scheduleRepo.countAllRunningSchedules();
			stoppedCount = scheduleRepo.countAllStoppedSchedules();
		}else {
			totalCount = scheduleRepo.countAllSchedulesOfOperator(user.get().getOperator());
			runningCount = scheduleRepo.countAllRunningSchedulesOfOperator(user.get().getOperator());
			stoppedCount = scheduleRepo.countAllStoppedSchedulesOfOperator(user.get().getOperator());
		}
		
		schedulesStats.put("totalCount", totalCount != null ? totalCount : 0);
		schedulesStats.put("runningCount", runningCount != null ? runningCount : 0);
		schedulesStats.put("stoppedCount", stoppedCount != null ? stoppedCount : 0);
		return new ResponseEntity<>(schedulesStats, HttpStatus.OK);
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> getUserStats(Principal principal){
		Map<String, Integer> userStats = new HashMap<>();
		
		Integer totalCount = userRepo.countAllUsers();
		Integer customersCount = userRepo.countAllCustomers(); 
		Integer operatorsCount = userRepo.countAllOperators();
		
		userStats.put("totalCount", totalCount != null ? totalCount : 0);
		userStats.put("customersCount", customersCount != null ? customersCount : 0);
		userStats.put("operatorsCount", operatorsCount != null ? operatorsCount : 0);
		
		return new ResponseEntity<>(userStats, HttpStatus.OK);
	}

}
