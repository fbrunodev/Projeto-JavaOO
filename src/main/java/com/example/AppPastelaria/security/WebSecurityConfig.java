package com.example.AppPastelaria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.AppPastelaria.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
	

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.authenticationProvider(authenticationProvider());
	}
	
	
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
         .csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(authz -> authz
             .requestMatchers(HttpMethod.POST,"admin/usuarios").hasRole("ADMIN")

          
             
             .anyRequest().authenticated()
             
         )
         .formLogin((form) -> form
	             .loginPage("/user/login")
	             .usernameParameter("email")
	             .defaultSuccessUrl("/home", true)
	             .permitAll()
         )
         .logout(logout -> logout.logoutSuccessUrl("/login"))
         .build();
         
	}
	

}
