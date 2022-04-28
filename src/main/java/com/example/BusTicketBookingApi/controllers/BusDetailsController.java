package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.BusDetailsRequest;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@Controller
@RequestMapping("/api/v1/bus_details")
public class BusDetailsController {
	
	@Autowired
	LocationRepo locationRepo;
	
	@Autowired
	ServiceDetailsRepo serviceDetailsRepo;
	
	@Autowired
	BusDetailsRepo busDetailsRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BasicUtil basicUtil;
	
	@GetMapping("/")
	public ResponseEntity<?> getBuses(Model model, Principal principal) {
				
		List<Map<String, String>> busesMap = new LinkedList<>();
		List<BusDetails> busDetails = busDetailsRepo.findAll();
	
		for(BusDetails busDetail: busDetails) {
			Map<String, String> busMap = new LinkedHashMap<>();

			busMap.put("Bus Id", String.valueOf(busDetail.getId()));
			busMap.put("Bus Name", busDetail.getBusName());
			busMap.put("Bus Reg Number", busDetail.getBusRegNumber());
			busMap.put("Bus Type", busDetail.getBusType());
			busMap.put("Seating Type", busDetail.getSeatingType());
			busMap.put("Seat Count", String.valueOf(busDetail.getSeatCount()));
			busMap.put("Operator", busDetail.getOperator().getOperator());
			busMap.put("Last Maintance", busDetail.getLastMaintance().toString());
			busMap.put("On Service", busDetail.getOnService().toString());
			
			busesMap.add(busMap);
		}
		
		return new ResponseEntity<>(busesMap, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> showBusDetails(@PathVariable int id ,Model model, Principal principal) {
		Optional<BusDetails> busDetails = busDetailsRepo.findById(id);
		
		if(busDetails.isPresent())
			return new ResponseEntity<BusDetails>(busDetails.get(), HttpStatus.ACCEPTED);
		else
			return new ResponseEntity<String>("Couldn't find Bus details with id " + id, HttpStatus.BAD_REQUEST);
	}
	
	
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody BusDetailsRequest busDetailsRequest, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		Optional<User> user = basicUtil.getUser(principal);
		
		if(user.isPresent()) {
			BusDetails busDetails = busDetailsRequest.getBusDetailsInstance();
			busDetails.setOperator(user.get());
			busDetails.generateBusName();
			busDetailsRepo.save(busDetails);
			return new ResponseEntity<BusDetails>(busDetails,HttpStatus.ACCEPTED);
		}else
			return new ResponseEntity<String>("Error creating bus details", HttpStatus.BAD_REQUEST);
	}
	
	/*
	@PostMapping("/{id}")
	public String update(@PathVariable int id, BusDetails busDetails,Model model, Principal principal, RedirectAttributes redirectAttributes) {
		basicUtil.addNavBarAttributesToModel(principal, model);
		
		Optional<User> user = basicUtil.getUser(principal);
		Optional<BusDetails> existingBusDetails = busDetailsRepo.findById(id);
		
		if(user.isPresent() && user.get().getRole().equals("ROLE_OPERATOR") && existingBusDetails.isPresent() && existingBusDetails.get().getOperator().getOperator().equals(user.get().getOperator())) {
			busDetails.setId(id);
			busDetails.setOperator(user.get());
			busDetails.generateBusName();
			busDetailsRepo.save(busDetails);
			basicUtil.addMsgToRedirectFlash(busDetails.getBusName() + " updated successfully!", "success", "show", redirectAttributes);
		}
		
		return "redirect:/bus_details/";
	}
	
	*/
}
