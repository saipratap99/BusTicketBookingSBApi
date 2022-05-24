package com.example.BusTicketBookingApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.BusTicketBookingApi.exceptions.BusDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.InvalidBookingDateException;
import com.example.BusTicketBookingApi.exceptions.InvalidWeekDayException;
import com.example.BusTicketBookingApi.exceptions.LocationNotFoundException;
import com.example.BusTicketBookingApi.exceptions.ScheduleDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.SeatingTypeNotFound;
import com.example.BusTicketBookingApi.exceptions.ServiceDetailsNotFoundException;
import com.example.BusTicketBookingApi.exceptions.UserNotFoundException;
import com.example.BusTicketBookingApi.exceptions.BookingDeatilsNotFound;
import com.example.BusTicketBookingApi.utils.BasicUtil;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
	
	@Autowired
	BasicUtil basicUtil;

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<?> expiredJWTTokenException(ExceptionHandler exception){
		System.out.println("Expired Token");
		return new ResponseEntity<String>("Token expired", HttpStatus.UNAUTHORIZED);	
	}
	
	@ExceptionHandler(value = BusDetailsNotFoundException.class)
	public ResponseEntity<?> busDetailsNotFoundException(BusDetailsNotFoundException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = BookingDeatilsNotFound.class)
	public ResponseEntity<?> bookingDetailsNotFoundException(BookingDeatilsNotFound exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = LocationNotFoundException.class)
	public ResponseEntity<?> locationNotFoundException(LocationNotFoundException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ScheduleDetailsNotFoundException.class)
	public ResponseEntity<?> scheduleDetailsNotFoundException(ScheduleDetailsNotFoundException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = SeatingTypeNotFound.class)
	public ResponseEntity<?> seatingTypeNotFoundException(SeatingTypeNotFound exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ServiceDetailsNotFoundException.class)
	public ResponseEntity<?> ServiceDetailsNotFoundException(ServiceDetailsNotFoundException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> userNotFoundException(UserNotFoundException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidWeekDayException.class)
	public ResponseEntity<?> invalidWeekDayException(InvalidWeekDayException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = DisabledException.class)
	public ResponseEntity<?> disabledException(DisabledException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", "Please activate your account by signing up again!") + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidBookingDateException.class)
	public ResponseEntity<?> invalidBookingDateException(InvalidBookingDateException exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> exception(Exception exception){
		return new ResponseEntity<String>("{" + basicUtil.getJSONString("msg", exception.getMessage()) + "}" , HttpStatus.BAD_REQUEST);
	}
	
}
