package com.example.BusTicketBookingApi.utils;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.BusTicketBookingApi.daos.BookingDeatilsRepo;
import com.example.BusTicketBookingApi.daos.ScheduleRepo;
import com.example.BusTicketBookingApi.daos.UserRepo;
import com.example.BusTicketBookingApi.models.BookingDetails;
import com.example.BusTicketBookingApi.models.BusDetails;
import com.example.BusTicketBookingApi.models.Schedule;
import com.example.BusTicketBookingApi.models.User;


@Component
public class BasicUtil {
	@Autowired 
	UserRepo userRepo;
	
	@Autowired
	ScheduleRepo scheduleRepo;
	
	@Autowired 
	BookingDeatilsRepo bookingDeatilsRepo;
	
	public Time parseStringToSqlTime(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return new Time(sdf.parse(time).getTime());
	}
	
	public Optional<User> getUser(Principal princpal){
		if(princpal == null)
			return Optional.ofNullable(null);
		return Optional.ofNullable(userRepo.findByEmail(princpal.getName()));		
	}
	
	public String getWeekDay(int week) {
		switch(week){
			case 1:
				return "Sunday";
			case 2:
				return "Monday";
			case 3:
				return "Tuesday";
			case 4:
				return "Wednesday";
			case 5:
				return "Thursday";
			case 6:
				return "Friday";
			case 7:
				return "Saturday";
		}
		return "";
	}
	
	public String getJSONString(String key, String value) {
		return "\""+ key + "\"" + ":" + "\"" + value + "\"";
	}
	
	public String getJSONString(String key, int value) {
		return "\""+ key + "\"" + ":" + value;
	}
	
	public String getJSONString(String key, double value) {
		return "\""+ key + "\"" + ":" + value;
	}
	
	public boolean isBothTimesAreEquals(Time t1, Time t2) {
		return t1.getTime() == t2.getTime();
	}
	
	public int generateFourDigitOTP() {
		return (int)(Math.random()* 10000);
	}
	
	public boolean isAdmin(User user) {
		return "ROLE_ADMIN".equalsIgnoreCase(user.getRole());
	}
	
	public boolean isOperator(User user) {
		return "ROLE_OPERATOR".equalsIgnoreCase(user.getRole());
	}
	
	public boolean isBusBelongsToOperator(BusDetails busDetails, User operator) {
		return operator.getOperator().equalsIgnoreCase(busDetails.getOperator().getOperator());
	}

	public boolean isScheduleBelongsToOperator(Schedule schedule, User operator) {
		return operator.getOperator().equalsIgnoreCase(schedule.getBusDetails().getOperator().getOperator());
	}
	
	
	public String deleteAllSchedules(List<Schedule> schedules,String cancellationFeedback ) {
		
		int schedulesCancelledCount = 0;
		int bookingsCancelledCount = 0;
		
		if(schedules == null)
			return "Schedules cancelled: " + schedulesCancelledCount + "and Bookings cancelled: 0";
		
		for(Schedule schedule: schedules) {
			schedule.setDeleted(true);
			scheduleRepo.save(schedule);
			schedulesCancelledCount++;
			List<BookingDetails> bookings = bookingDeatilsRepo.findByScheduleAndStatus(schedule, "SUCCESS");
			bookingsCancelledCount += cancelAllUpComingBookingsIfBooked(bookings, cancellationFeedback);
		}
		
		return "Schedules cancelled: " + schedulesCancelledCount + " and Bookings cancelled: " + bookingsCancelledCount;
		
	}

	public int cancelAllUpComingBookingsIfBooked(List<BookingDetails> bookingDetails, String cancellationFeedback) {
		
		int bookingsCancelledCount = 0;
		
		if(bookingDetails == null)
			return bookingsCancelledCount;
		
		for(BookingDetails booking: bookingDetails) {
			Date currDate = new Date(System.currentTimeMillis());
			
			if(booking.getDoj().after(currDate)) {
				booking.setCancellationFeedback(cancellationFeedback);
				booking.setStatus("CANCELED");
				bookingsCancelledCount++;
				bookingDeatilsRepo.save(booking);
			}
		}
		
		return bookingsCancelledCount;
	}
	
	
	public boolean isDateBeforeCurrDate(Date date) {
		Date currDate = new Date(System.currentTimeMillis());
		
		LocalDate currLocalDate = currDate.toLocalDate();
		LocalDate localDate = date.toLocalDate();
		
		if(localDate.getDayOfMonth() == currLocalDate.getDayOfMonth() && localDate.getMonthValue() == currLocalDate.getMonthValue() && localDate.getYear() == currLocalDate.getYear())
			return false;
		
		return date.before(currDate);
	}
	
	public boolean isDateAfterNDays(Date date, int nDays) {
		Date currDate = new Date(System.currentTimeMillis());
		
		LocalDate currLocalDate = currDate.toLocalDate();
		currLocalDate = currLocalDate.plusDays(nDays);
		
//		currDate.setDate(currLocalDate.getDayOfMonth());
//		currDate.setMonth(currLocalDate.getMonthValue());
//		currDate.setYear(currLocalDate.getYear());
		
		LocalDate localDate = date.toLocalDate();
		
//		if(localDate.getDayOfMonth() == currLocalDate.getDayOfMonth() && localDate.getMonthValue() == currLocalDate.getMonthValue() && localDate.getYear() == currLocalDate.getYear())
//			return false;
		System.out.println(currLocalDate.toString() + " " + localDate.toString());
		return localDate.isAfter(currLocalDate);
	}
	

	
}
