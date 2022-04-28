package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.Location;
import com.example.BusTicketBookingApi.models.ServiceDetails;
import com.example.BusTicketBookingApi.models.ServiceDetailsRequest;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.utils.BasicUtil;



@Controller
@RequestMapping("api/v1/service_details")
public class ServiceDetailsController {
	
	@Autowired
	LocationRepo locationRepo;
	
	@Autowired
	ServiceDetailsRepo serviceDetailsRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	@GetMapping("/")
	public ResponseEntity<?> index(Model model, Principal principal) {
		
		List<Map<String, String>> servicesMap = new LinkedList<>();
		List<ServiceDetails> serviceDetails = serviceDetailsRepo.findAll();
	
		for(ServiceDetails serviceDetail: serviceDetails) {
			Map<String, String> serviceMap = new LinkedHashMap<>();
			
			serviceMap.put("Service Id", String.valueOf(serviceDetail.getId()));
			serviceMap.put("Service Number", serviceDetail.getServiceNumber());
			serviceMap.put("Service Name", serviceDetail.getServiceName());
			serviceMap.put("Service Type", serviceDetail.getServiceType());
			serviceMap.put("Departure Location", serviceDetail.getDepartureLocation().getLocationName());
			serviceMap.put("Arrival Location", serviceDetail.getArrivalLocation().getLocationName());
			serviceMap.put("Distance", String.valueOf(serviceDetail.getDistance()));
			
			servicesMap.add(serviceMap);
		}
		
		
		return new ResponseEntity<>(servicesMap, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> show(@PathVariable int id, Model model, Principal principal) {
		Optional<ServiceDetails> serviceDetails = serviceDetailsRepo.findById(id);		
		
		if(!serviceDetails.isPresent())
			return new ResponseEntity<String>("Couldn't find Service Details with id " + id, HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<ServiceDetails>(serviceDetails.get(), HttpStatus.ACCEPTED);
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ServiceDetailsRequest serviceDetailRequest, Principal principal, RedirectAttributes redirectAttributes) {
		
		
		Optional<Location> departureLocation = locationRepo.findByLocationName(serviceDetailRequest.getDepartureLocation());
		Optional<Location> arrivalLocation = locationRepo.findByLocationName(serviceDetailRequest.getArrivalLocation());
		
		User user = userRepo.findByEmail(principal.getName());
		
		if(user != null && departureLocation.isPresent() && arrivalLocation.isPresent()) {
			ServiceDetails serviceDetail = serviceDetailRequest.getServiceDetailsInstance();
			serviceDetail.setDepartureLocation(departureLocation.get());
			serviceDetail.setArrivalLocation(arrivalLocation.get());
			serviceDetail.genrateServiceName();
			serviceDetailsRepo.save(serviceDetail);
			return new ResponseEntity<ServiceDetails>(serviceDetail, HttpStatus.ACCEPTED);
		}else
			return new ResponseEntity<String>("Error creating service details", HttpStatus.BAD_REQUEST);
		
		
	}
	
	/*		
	@PostMapping("/{id}")
	public String update(@PathVariable int id, ServiceDetails serviceDetail, String depLocation, String arrLocation, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		Optional<ServiceDetails> existingServiceDetails = serviceDetailsRepo.findById(id);		
		if(!existingServiceDetails.isPresent())
			return "redirect:/service_details/";
		
		Optional<Location> departureLocation = locationRepo.findByLocationName(depLocation);
		Optional<Location> arrivalLocation = locationRepo.findByLocationName(arrLocation);
		
		Optional<User> user = basicUtil.getUser(principal);
		
		if(user.isPresent() && departureLocation.isPresent() && arrivalLocation.isPresent() && user.get().getRole().equals("ROLE_OPERATOR")) {
			serviceDetail.setId(id);
			serviceDetail.setDepartureLocation(departureLocation.get());
			serviceDetail.setArrivalLocation(arrivalLocation.get());
			serviceDetail.genrateServiceName();
			serviceDetailsRepo.save(serviceDetail);
			basicUtil.addMsgToRedirectFlash(serviceDetail.getServiceName() + " updated successfully!", "success", "show", redirectAttributes);
		}
		
		return "redirect:/service_details/";
	
	}
	*/
}
