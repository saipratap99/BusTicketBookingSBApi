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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.BusTicketBookingApi.filters.CorsFilter;



@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	CorsFilter corsFilter;
	
//	@Autowired
//	JwtRequestFilter jwtRequestFilter;
	
//	@Autowired
//	SessionRequestFilter sessionRequestFilter;
	
//	@Autowired
//	PropertiesUtil propertiesUtil;
	

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
//		      return cors;
//		});
		
		http.csrf().disable()
			.authorizeHttpRequests()
			.antMatchers("/**/")
			.permitAll()
			.anyRequest()
			.authenticated();
		
		/*
		if(propertiesUtil.isSessionsBasedAuth())
			http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
		if(propertiesUtil.isJWTBasedAuth())
		*/
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		/*
		http.exceptionHandling().authenticationEntryPoint((request, response, authException)->{	
			response.sendRedirect("/users/login?msg=Please+login&status=danger&show=show");
		});
		
		
		if(propertiesUtil.isJWTBasedAuth()) {	
			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
		if(propertiesUtil.isSessionsBasedAuth())
			http.addFilterBefore(sessionRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		http
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/users/logout", "POST"))
			.clearAuthentication(true)
			.deleteCookies("jwt","SESSION")
			.logoutSuccessUrl("/users/login?msg=User+logged+out&status=success&show=show");
		*/
//	     http.addFilterAfter(corsFilter, UsernamePasswordAuthenticationFilter.class);
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
	
	
}
