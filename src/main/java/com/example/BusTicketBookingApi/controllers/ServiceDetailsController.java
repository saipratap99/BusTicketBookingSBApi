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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.exceptions.LocationNotFoundException;
import com.example.BusTicketBookingApi.exceptions.ServiceDetailsNotFoundException;
import com.example.BusTicketBookingApi.models.Location;
import com.example.BusTicketBookingApi.models.ServiceDetails;
import com.example.BusTicketBookingApi.models.ServiceDetailsRequest;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.utils.BasicUtil;



@RestController
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
		try {
			List<Map<String, String>> servicesMap = new LinkedList<>();
			List<ServiceDetails> serviceDetails = serviceDetailsRepo.findAll();
		
			for(ServiceDetails serviceDetail: serviceDetails) {
				Map<String, String> serviceMap = new LinkedHashMap<>();
				
				serviceMap.put("id", String.valueOf(serviceDetail.getId()));
				serviceMap.put("serviceNumber", serviceDetail.getServiceNumber());
				serviceMap.put("serviceName", serviceDetail.getServiceName());
				serviceMap.put("serviceType", serviceDetail.getServiceType());
				serviceMap.put("departureLocation", serviceDetail.getDepartureLocation().getLocationName());
				serviceMap.put("arrivalLocation", serviceDetail.getArrivalLocation().getLocationName());
				serviceMap.put("distance", String.valueOf(serviceDetail.getDistance()));
				
				servicesMap.add(serviceMap);
			}
			return new ResponseEntity<>(servicesMap, HttpStatus.ACCEPTED);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> show(@PathVariable int id, Model model, Principal principal) {
		try {
			Optional<ServiceDetails> serviceDetails = serviceDetailsRepo.findById(id);		
			serviceDetails.orElseThrow(() -> new ServiceDetailsNotFoundException("Service details with id " + id + " not found"));
			return new ResponseEntity<>(serviceDetails.get(), HttpStatus.OK);
		}catch(ServiceDetailsNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ServiceDetailsRequest serviceDetailRequest, Principal principal, RedirectAttributes redirectAttributes) {
		try {

			Optional<Location> departureLocation = locationRepo.findByLocationName(serviceDetailRequest.getDepartureLocation());
			Optional<Location> arrivalLocation = locationRepo.findByLocationName(serviceDetailRequest.getArrivalLocation());
			departureLocation.orElseThrow(() -> new LocationNotFoundException("Departure location " + serviceDetailRequest.getDepartureLocation() + " not found"));
			arrivalLocation.orElseThrow(() -> new LocationNotFoundException("Arrival location " + serviceDetailRequest.getArrivalLocation() + " not found"));
			
			ServiceDetails serviceDetail = serviceDetailRequest.getServiceDetailsInstance();
			serviceDetail.setDepartureLocation(departureLocation.get());
			serviceDetail.setArrivalLocation(arrivalLocation.get());
			serviceDetail.genrateServiceName();
			serviceDetailsRepo.save(serviceDetail);
			
			return new ResponseEntity<ServiceDetails>(serviceDetail, HttpStatus.ACCEPTED);
		}catch(LocationNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<?> update(@PathVariable int id, @RequestBody ServiceDetailsRequest serviceDetailRequest, Principal principal) {
		
		try {
			Optional<User> user = basicUtil.getUser(principal);
			
			Optional<ServiceDetails> existingServiceDetails = serviceDetailsRepo.findById(id);		
			existingServiceDetails.orElseThrow(() -> new ServiceDetailsNotFoundException("Service details with id " + id + " not found"));
			
			
			Optional<Location> departureLocation = locationRepo.findByLocationName(serviceDetailRequest.getDepartureLocation());
			Optional<Location> arrivalLocation = locationRepo.findByLocationName(serviceDetailRequest.getArrivalLocation());
			departureLocation.orElseThrow(() -> new LocationNotFoundException("Departure location " + serviceDetailRequest.getDepartureLocation() + " not found"));
			arrivalLocation.orElseThrow(() -> new LocationNotFoundException("Arrival location " + serviceDetailRequest.getArrivalLocation() + " not found"));
			
			ServiceDetails serviceDetail = serviceDetailRequest.getUpdatedServiceDetailsInstance(existingServiceDetails.get());
			serviceDetail.setDepartureLocation(departureLocation.get());
			serviceDetail.setArrivalLocation(arrivalLocation.get());
			serviceDetail.genrateServiceName();
			
			serviceDetailsRepo.save(serviceDetail);
			
			return new ResponseEntity<ServiceDetails>(serviceDetail, HttpStatus.OK);
			
		}catch(ServiceDetailsNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
	
	}
	
}
