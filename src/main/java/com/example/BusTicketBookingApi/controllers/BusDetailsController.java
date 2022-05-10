package com.example.BusTicketBookingApi.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Validation;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.BusDetailsRepo;
import com.example.BusTicketBookingApi.daos.LocationRepo;
import com.example.BusTicketBookingApi.daos.SeatingTypeRepo;
import com.example.BusTicketBookingApi.daos.ServiceDetailsRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.exceptions.BusDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.SeatingTypeNotFound;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.SeatingType;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.models.requests.BusDetailsRequest;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@RestController
@RequestMapping("/api/v1/bus_details")
public class BusDetailsController {
	

	@Autowired
	SeatingTypeRepo seatingTypeRepo;
	
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
		try {
			List<Map<String, String>> busesMap = new LinkedList<>();
			List<BusDetails> busDetails = busDetailsRepo.findAll();
		
			for(BusDetails busDetail: busDetails) {
				Map<String, String> busMap = new LinkedHashMap<>();

				busMap.put("id", String.valueOf(busDetail.getId()));
				busMap.put("busName", busDetail.getBusName());
				busMap.put("busRegNumber", busDetail.getBusRegNumber());
				busMap.put("busType", busDetail.getBusType());
				busMap.put("seatingType", busDetail.getSeatingType().getSeating());
//				busMap.put("seatCount", String.valueOf(busDetail.getSeatCount()));
				busMap.put("operator", busDetail.getOperator().getOperator());
				busMap.put("lastMaintance", busDetail.getLastMaintance().toString());
				busMap.put("onService", busDetail.getOnService().toString());
				
				busesMap.add(busMap);
			}
			
			return new ResponseEntity<>(busesMap, HttpStatus.ACCEPTED);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> showBusDetails(@PathVariable int id ,Model model, Principal principal) {
		try {
			Optional<BusDetails> busDetails = busDetailsRepo.findById(id);
			busDetails.orElseThrow(() -> new BusDetailsNotFoundException("Couldn't find Bus details with id " + id));
			return new ResponseEntity<BusDetails>(busDetails.get(), HttpStatus.ACCEPTED);
		}catch(BusDetailsNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody BusDetailsRequest busDetailsRequest, Principal principal) {
		
		try {
			
			Optional<User> user = basicUtil.getUser(principal);
			Optional<SeatingType> seatingType = seatingTypeRepo.findBySeating(busDetailsRequest.getSeatingType());
			seatingType.orElseThrow(() -> new SeatingTypeNotFound(busDetailsRequest.getSeatingType() +  "  seating type not found"));
			
			BusDetails busDetails = busDetailsRequest.getBusDetailsInstance(seatingType.get());
			busDetails.setOperator(user.get());
			busDetails.generateBusName();
			
			busDetailsRepo.save(busDetails);
			return new ResponseEntity<BusDetails>(busDetails,HttpStatus.ACCEPTED);
		}catch(SeatingTypeNotFound e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
		
			
//		}else
//			return new ResponseEntity<String>("Error creating bus details", HttpStatus.BAD_REQUEST);
	}
	
	
	@PutMapping("edit/{id}")
	public ResponseEntity<?> update(@PathVariable int id, @RequestBody BusDetailsRequest busDetailsRequest, Principal principal) {
		try {

			Optional<User> user = basicUtil.getUser(principal);
			Optional<BusDetails> existingBusDetails = busDetailsRepo.findById(id);
			existingBusDetails.orElseThrow(() -> new BusDetailsNotFoundException("Couldn't find Bus details with id " + id));
			
			Optional<SeatingType> seatingType = seatingTypeRepo.findBySeating(busDetailsRequest.getSeatingType());
			seatingType.orElseThrow(() -> new SeatingTypeNotFound(busDetailsRequest.getSeatingType() +  "  seating type not found"));
			
			BusDetails busDetails = busDetailsRequest.updateBusDetailsInstance(existingBusDetails.get() ,seatingType.get());
			
			busDetails.generateBusName();
			busDetailsRepo.save(busDetails);
			return new ResponseEntity<BusDetails>(busDetails,HttpStatus.ACCEPTED);
		}catch(BusDetailsNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(SeatingTypeNotFound e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", e.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
		}
		
//		if(user.isPresent() && user.get().getRole().equals("ROLE_OPERATOR") && existingBusDetails.isPresent() && existingBusDetails.get().getOperator().getOperator().equals(user.get().getOperator())) {
//			busDetails.setId(id);
//			busDetails.setOperator(user.get());
//			busDetails.generateBusName();
//			busDetailsRepo.save(busDetails);
//		}
		
//		return "redirect:/bus_details/";
	}
	
	
}
