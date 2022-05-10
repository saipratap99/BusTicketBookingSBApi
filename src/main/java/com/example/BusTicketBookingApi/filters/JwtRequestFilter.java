package com.example.BusTicketBookingApi.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.BusTicketBookingApi.utils.JwtUtil;
import com.example.BusTicketBookingApi.utils.PropertiesUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PropertiesUtil propertiesUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(propertiesUtil.isJWTBasedAuth())
			authorizeUsingJwtFromCookieOrHeader(request, response);
		
		filterChain.doFilter(request, response);
		
	}
	
	public void authorizeUsingJwtFromCookieOrHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String authorizationHeader = request.getHeader("Authorization"); 
			String username = null;
			String jwt = null;
			
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			}else if(request.getCookies() != null) {	
				for(Cookie cookie: request.getCookies()) 
					if(cookie != null && cookie.getName().equalsIgnoreCase("jwt"))
						authorizationHeader = "Bearer " + cookie.getValue();	
			}
			
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if(jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);
					
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					response.addHeader("Access-Control-Expose-Headers", "Authorization");
					response.setHeader("Authorization", "Bearer " + jwt);
				}
			}
			
		}catch(ExpiredJwtException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
}
