package com.example.BusTicketBookingApi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.BusTicketBookingApi.models.User;
import com.example.BusTicketBookingApi.utils.BasicUtil;

@Service
public class EmailSenderService{
	
	@Autowired
	JavaMailSender emailSender;
	
	@Autowired
	BasicUtil basicUtil;
	
	public void sendSignUpMail(User user) {
		SimpleMailMessage otpMail = new SimpleMailMessage();
		
		otpMail.setFrom("bus.ticket.booking.99@gmail.com");
		otpMail.setTo(user.getEmail());
		otpMail.setSubject("Otp for signup");
		otpMail.setText("Hi " + user.getFirstName() +"\nYour OTP for signup is: " + user.getOTP() );
		
		emailSender.send(otpMail);
	}
}
