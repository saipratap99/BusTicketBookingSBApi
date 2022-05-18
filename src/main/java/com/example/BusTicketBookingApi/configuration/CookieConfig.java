package com.example.BusTicketBookingApi.configuration;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CookieConfig {
	@Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone();
    }
}
