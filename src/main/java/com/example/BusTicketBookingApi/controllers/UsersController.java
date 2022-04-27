package com.example.BusTicketBookingApi.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.User;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {
	
	@Autowired
	UserRepo userRepo;
	
	@GetMapping("")
	public String index() {
		return "<h2>Index</h2>";
	}
	
	@PostMapping("/create")
	public User createUser(@Valid @RequestBody User user,
							   BindingResult result,
							   HttpServletResponse response,
							   Principal principal, 
							   RedirectAttributes redirectAttributes) throws IOException {

		String msg = "", status = "danger";
		
		System.out.println(user.getFirstName() + " " + user.getConfirmPassword() + " " + user.getPassword());
		if(result.hasErrors()) {
			for(FieldError error: result.getFieldErrors()) 
				msg += error.getField() + ": " + error.getDefaultMessage() + "<br>";
			System.out.println(msg);
		}else {
			System.out.println("no errors");
			if(userRepo.findByEmail(user.getEmail()) != null)
				msg += "Email already exsits";
			else if(!user.getPassword().equals(user.getConfirmPassword()))
				msg += "Password must be same";
			else {
				user.setRole("ROLE_USER");
				userRepo.save(user);
				msg += "User " + user.getFirstName() + " has been added!";
				status = "success";
			}
		}
		
		redirectAttributes.addFlashAttribute("msg", msg);
		redirectAttributes.addFlashAttribute("status", status);
		redirectAttributes.addFlashAttribute("show", "show");
		
		return user;
	}
}
