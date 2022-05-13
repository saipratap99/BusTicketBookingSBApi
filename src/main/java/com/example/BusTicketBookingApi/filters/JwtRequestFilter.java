package com.example.BusTicketBookingApi.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.BusTicketBookingApi.exceptions.ExpiredSessionException;
import com.example.BusTicketBookingApi.utils.JwtUtil;
import com.example.BusTicketBookingApi.utils.PropertiesUtil;
import com.example.BusTicketBookingApi.utils.RefreshJwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	RefreshJwtUtil refreshJwtUtil;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PropertiesUtil propertiesUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(propertiesUtil.isJWTBasedAuth()) {
			try {
				authorizeUsingJwtFromCookieOrHeader(request, response);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ExpiredSessionException e) {
				e.printStackTrace();
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
	public void authorizeUsingJwtFromCookieOrHeader(HttpServletRequest request, HttpServletResponse response) throws IOException, ExpiredSessionException {
		try {
			String authorizationHeader = request.getHeader("Authorization"); 
			String username = null;
			String jwt = null;
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			}else if(request.getCookies() != null) {	
				jwt = getCookie("jwt", request);
				username = jwtUtil.extractUsername(jwt);
			}
			
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) 
				validateJWTAndSetSecurityContext(username, jwt, request, response);
			
			
		}catch(ExpiredJwtException e) {
			String refreshToken = getCookie("refreshToken", request);
			
			if(refreshToken != null) {
				
				String username = refreshJwtUtil.extractUsername(refreshToken);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				if(!refreshJwtUtil.validateToken(refreshToken, userDetails)) 
					throw new ExpiredSessionException("Session expired. Please Login again!");
				
				validateJWTAndSetSecurityContext(username, refreshJwtUtil.reGenerateAccessToken(userDetails), request, response);
			}
			
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void validateJWTAndSetSecurityContext(String username, String jwt, HttpServletRequest request, HttpServletResponse response) {
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

	public String getCookie(String name, HttpServletRequest request) {
		Cookie cookies[] = request.getCookies();
		if(cookies == null)
			return null;
		for(Cookie cookie: cookies)
			if(cookie != null && cookie.getName().equalsIgnoreCase(name))
				return cookie.getValue();
		return null;
	}
}
