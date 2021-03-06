package com.example.BusTicketBookingApi.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.exceptions.IncorrectOTPException;
import com.example.BusTicketBookingApi.exceptions.UserNotFoundException;
import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.models.requests.AuthenticationRequest;
import com.example.BusTicketBookingApi.models.responses.AuthenticationResponse;
import com.example.BusTicketBookingApi.services.EmailSenderService;
import com.example.BusTicketBookingApi.services.UserService;
import com.example.BusTicketBookingApi.utils.BasicUtil;
import com.example.BusTicketBookingApi.utils.JwtUtil;
import com.example.BusTicketBookingApi.utils.PropertiesUtil;
import com.example.BusTicketBookingApi.utils.RefreshJwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {
	
	@Value("${app.cookie.domain}")
	private String cookieDomain;
	
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
	
	@Autowired
	RefreshJwtUtil refreshJwtUtil;
	
	@Autowired
	BasicUtil basicUtil;
	
	@Autowired
	EmailSenderService emailService;
	
	@PostMapping("/{id}/alter-role")
	public ResponseEntity<?> makeOperator(@PathVariable int id,@RequestParam String operatorName, Principal principal) throws Exception {
		
		Optional<User> currentUser = basicUtil.getUser(principal);
		Optional<User> user = userRepo.findById(id);
		
		user.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
		
		if(!("ROLE_ADMIN".equals(currentUser.get().getRole()))) 
			throw new Exception("Access denied");
		
		if(user.get().getRole().equalsIgnoreCase("ROLE_USER")) {
			user.get().setRole("ROLE_OPERATOR");
			user.get().setOperator(operatorName.toUpperCase());
		}else {
			user.get().setRole("ROLE_USER");
			user.get().setOperator(null);
		}
		userRepo.save(user.get());	
		
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Successfully changed to " + (user.get().getRole().equalsIgnoreCase("ROLE_OPERATOR") ? "Operator" : "User")) + "}" , HttpStatus.OK);
		
		
	}
	
	@GetMapping("/{id}/email")
	public ResponseEntity<?> getEmail(@PathVariable int id) throws UserNotFoundException{
		Optional<User> user = userRepo.findById(id);
		user.orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
		
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("email", user.get().getEmail())	 + "}" , HttpStatus.OK); 
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllUsers(Principal principal) throws Exception{
		 Optional<User> currUser = basicUtil.getUser(principal);
		 currUser.orElseThrow(() -> new UserNotFoundException("User not found"));
		 if(!currUser.get().getRole().equalsIgnoreCase("ROLE_ADMIN"))
			 throw new Exception("Access denied");
		 
		 List<Map<String, String>> usersMap = new LinkedList<>();
		 List<User> users = userRepo.findAll();
		 for(User user: users) {
			 if(!user.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
				 Map<String, String> userMap = new LinkedHashMap<>();
				 userMap.put("id", String.valueOf(user.getId()));
				 userMap.put("name", user.getFirstName() + " " + user.getLastName());
				 userMap.put("email", user.getEmail());
				 userMap.put("role",user.getRole() );
				 usersMap.add(userMap);
			 }
			 
		 }
		 return new ResponseEntity<>(usersMap, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user,
							   BindingResult result,
							   HttpServletResponse response,
							   Principal principal, 
							   RedirectAttributes redirectAttributes) throws IOException {

		String msg = "";
		ResponseEntity<?> responseEntity = null;
		
		System.out.println("CREATE");
		if(result.hasErrors()) {
			for(FieldError error: result.getFieldErrors()) 
				msg += error.getField() + ": " + error.getDefaultMessage() + "<br>";
			responseEntity = new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", msg)+ "}", HttpStatus.BAD_REQUEST);
		
		}else {
			User existingUser = userRepo.findByEmail(user.getEmail());
			if(existingUser != null && existingUser.isActivated())
				responseEntity = new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Email already exsits") + "}", HttpStatus.BAD_REQUEST);
			
			else if(!user.getPassword().equals(user.getConfirmPassword()))
				responseEntity = new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Password must be same") + "}", HttpStatus.BAD_REQUEST);
			
			else {
				if(existingUser != null && !existingUser.isActivated())
					user.setId(existingUser.getId());
				
				user.setRole("ROLE_USER");
				user.setOTP(basicUtil.generateFourDigitOTP());
				userService.save(user);
				emailService.sendSignUpMail(user);
				responseEntity = new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
			}
		}
		return responseEntity;
	}
	
	@PostMapping("/login")
	public <T> ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest,
									HttpServletResponse resp, HttpServletRequest request) throws UsernameNotFoundException {
		String jwt = "";
		String jwtRefreshToken = "";
		try {
			// authenticate if credentials are matching
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())	
			);
			
			// if matched gets the UserDetails object 
			UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			
			if(propertiesUtil.isJWTBasedAuth()) {
				jwt = generateJWTAccessToken(userDetails, resp);
				jwtRefreshToken = generateJWTRefreshToken(userDetails, resp);
			}
			
			Cookie refreshCookie = new Cookie("refreshToken", jwtRefreshToken);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setPath("/");
			refreshCookie.setSecure(true);	
			
			resp.addCookie(refreshCookie);
			
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
			);
			
			
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt, jwtRefreshToken),HttpStatus.ACCEPTED);
			
		}catch(BadCredentialsException e) {
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Invalid Email or Password")	 + "}" , HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("{id}/verify/otp/{otp}")
	public ResponseEntity<?> verifyOTP(@PathVariable int id, @PathVariable int otp) throws UserNotFoundException, IncorrectOTPException{
		Optional<User> user = userRepo.findById(id);
		user.orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
		if(otp == user.get().getOTP()) {
			user.get().setActivated(true);
			userRepo.save(user.get());
		}else
			throw new IncorrectOTPException("Incorrect OTP. Please try again.");
		
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "OTP Verified")	 + "}" , HttpStatus.OK);
	}
	
	@GetMapping("{id}/resend/otp")
	public ResponseEntity<?> resendOTP(@PathVariable int id) throws UserNotFoundException{
		Optional<User> user = userRepo.findById(id);
		user.orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
		
		emailService.sendSignUpMail(user.get());
		
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "OTP sent successfully.") + "}" , HttpStatus.OK);
	}
		
	
	
	@PostMapping("/auth/refresh-token/{refreshToken}")
	private ResponseEntity<?> generateNewAccessToken(@PathVariable String refreshToken, HttpServletResponse response){
		try {
		   	
			System.out.println(refreshToken);
			String jwtAccessToken = "";
			String userName = refreshJwtUtil.extractUsername(refreshToken);
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			refreshJwtUtil.validateToken(refreshToken, userDetails);
			jwtAccessToken = generateJWTAccessToken(userDetails, response);

			return new ResponseEntity<String>("{" + basicUtil.getJSONString("accessToken", jwtAccessToken) + ", " + basicUtil.getJSONString("refreshToken", refreshToken) + "}" , HttpStatus.BAD_REQUEST);
		}catch(ExpiredJwtException e) {
			System.out.println("Expired");
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Session Expired. Please Login agian!") + "}" , HttpStatus.UNAUTHORIZED);
		}catch(Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
			return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Please Login agian!") + "}" , HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	
	public String generateJWTAccessToken(UserDetails userDetails, HttpServletResponse response) {
		String jwt = jwtUtil.generateToken(userDetails);
		response.addHeader("Authorization", "Bearer " + jwt);
		return jwt;
	}
	
	public String generateJWTRefreshToken(UserDetails userDetails, HttpServletResponse response) {
		String jwtRefreshToken = refreshJwtUtil.generateToken(userDetails);
		return jwtRefreshToken;
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logOut(HttpServletResponse response){
		Cookie refreshCookie = new Cookie("refreshToken", "");
		refreshCookie.setPath("/");
		refreshCookie.setMaxAge(0);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(true);
		
		response.addCookie(refreshCookie);
		
		return new ResponseEntity<>("{" + basicUtil.getJSONString("msg", "Logged out successfully") + "}", HttpStatus.OK);
	}

}
