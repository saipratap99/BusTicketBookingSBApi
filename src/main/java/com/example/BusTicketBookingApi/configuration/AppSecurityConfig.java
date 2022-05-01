package com.example.BusTicketBookingApi.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.BusTicketBookingApi.filters.JwtRequestFilter;
import com.example.BusTicketBookingApi.utils.PropertiesUtil;



@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
//	@Autowired
//	SessionRequestFilter sessionRequestFilter;
	
	@Autowired
	PropertiesUtil propertiesUtil;
	

	// Authentication	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	// Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
//		http.cors().configurationSource(request -> {
//			  CorsConfiguration cors = new CorsConfiguration();
//		      cors.setAllowedOrigins(List.of("http://localhost:4200/"));
//		      cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
//		      cors.setAllowedHeaders(List.of("*"));
//		      cors.setExposedHeaders(List.of("*"));
//		      return cors;
//		});
		
		http.cors();
		
		http.csrf().disable()
			.authorizeHttpRequests()
			.antMatchers("/api/*/admin/**").hasRole("ADMIN")
			.antMatchers("/api/*/bus_details/**", "/schedule/**", "/service_details/**").hasAnyRole("ADMIN", "OPERATOR","USER")
			.antMatchers("/api/*/bookings/**").hasAnyRole("ADMIN", "OPERATOR","USER")
			.antMatchers("/api/*/authenticate","/api/*/users/*").permitAll()
			.anyRequest()
			.authenticated();
		
		
		if(propertiesUtil.isSessionsBasedAuth())
			http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		if(propertiesUtil.isJWTBasedAuth())
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		if(propertiesUtil.isJWTBasedAuth()) {	
			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
		
//		if(propertiesUtil.isSessionsBasedAuth())
//			http.addFilterBefore(sessionRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		/*
		http.exceptionHandling().authenticationEntryPoint((request, response, authException)->{	
			response.sendRedirect("/users/login?msg=Please+login&status=danger&show=show");
		});
		
		
		
		http
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/users/logout", "POST"))
			.clearAuthentication(true)
			.deleteCookies("jwt","SESSION")
			.logoutSuccessUrl("/users/login?msg=User+logged+out&status=success&show=show");
		*/
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	/*
	@Bean
	public org.springframework.web.filter.CorsFilter corsConfigurationSource() {
		System.out.println("cors config");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		final CorsConfiguration corsConfigurationSource = new CorsConfiguration();
		
		corsConfigurationSource.addExposedHeader("*");
		corsConfigurationSource.addAllowedHeader("*");
		corsConfigurationSource.addAllowedMethod("*");
		corsConfigurationSource.addAllowedOrigin("*");
		corsConfigurationSource.addAllowedHeader("*");
		
		
		source.registerCorsConfiguration("/**", corsConfigurationSource);
		return new org.springframework.web.filter.CorsFilter((org.springframework.web.cors.CorsConfigurationSource) corsConfigurationSource);	
	}
	*/
	
	
}
