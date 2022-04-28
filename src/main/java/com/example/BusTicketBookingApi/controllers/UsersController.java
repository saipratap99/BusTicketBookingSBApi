package com.example.BusTicketBookingApi.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.AuthenticationRequest;
import com.example.BusTicketBookingApi.models.AuthenticationResponse;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.services.UserService;
import com.example.BusTicketBookingApi.utils.JwtUtil;
import com.example.BusTicketBookingApi.utils.PropertiesUtil;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PropertiesUtil propertiesUtil;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@GetMapping("")
	public String index() {
		return "<h2>Index</h2>";
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user,
							   BindingResult result,
							   HttpServletResponse response,
							   Principal principal, 
							   RedirectAttributes redirectAttributes) throws IOException {

		String msg = "";
		ResponseEntity<?> responseEntity = null;
		
		if(result.hasErrors()) {
			for(FieldError error: result.getFieldErrors()) 
				msg += error.getField() + ": " + error.getDefaultMessage() + "<br>";
			responseEntity = new ResponseEntity<String>(msg,HttpStatus.BAD_REQUEST);
		
		}else {
			if(userRepo.findByEmail(user.getEmail()) != null)
				responseEntity = new ResponseEntity<String>("Email already exsits",HttpStatus.BAD_REQUEST);
		
			else if(!user.getPassword().equals(user.getConfirmPassword()))
				responseEntity = new ResponseEntity<String>("Password must be same",HttpStatus.BAD_REQUEST);
			
			else {
				user.setRole("ROLE_USER");
				userService.save(user);
				responseEntity = new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
			}
		}
		return responseEntity;
	}
	
	@CrossOrigin(exposedHeaders = "Authorization")
	@PostMapping("/login")
	public <T> ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest,
									RedirectAttributes redirectAttributes,
									HttpServletResponse resp, HttpServletRequest request) throws UsernameNotFoundException {
		String jwt = "";
		try {
			// authenticate if credentials are matching
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())	
			);
			
			// if matched gets the UserDetails object 
			UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			
			if(propertiesUtil.isJWTBasedAuth())
				jwt = generateJWTAccessToken(userDetails, resp);
			

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
			);
			
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt),HttpStatus.ACCEPTED);
			
		}catch(BadCredentialsException e) {
			redirectAttributes.addFlashAttribute("msg", "Invalid email or password");
			redirectAttributes.addFlashAttribute("status","danger");
			redirectAttributes.addFlashAttribute("show","show");
			
			return new ResponseEntity<String>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	public String generateJWTAccessToken(UserDetails userDetails, HttpServletResponse response) {
		String jwt = jwtUtil.generateToken(userDetails);
		response.addHeader("Authorization", "Bearer " + jwt);
		return jwt;
	}

}
