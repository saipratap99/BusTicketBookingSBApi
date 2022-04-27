package com.example.BusTicketBookingApi.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {
	@Value("${app.authentication.type}")
	private String authenticationType;

	public boolean isJWTBasedAuth() {
		return "jwt".equalsIgnoreCase(authenticationType);
	}
	
	public boolean isSessionsBasedAuth() {
		return "sessions".equalsIgnoreCase(authenticationType);
	}
}
