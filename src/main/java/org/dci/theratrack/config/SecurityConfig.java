package org.dci.theratrack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeRequests -> {
          authorizeRequests
              .requestMatchers("/api/patients/**").hasRole("PATIENT")

              .requestMatchers("/api/therapists/**").hasRole("THERAPIST")  // Access therapist's own profile
              .requestMatchers("/api/patients/**").hasRole("THERAPIST")    // Therapists can access patient profiles
              .requestMatchers("/api/appointments/**").hasRole("THERAPIST")  // Therapists can view appointments


              .requestMatchers("/api/admin/**").hasRole("ADMIN")
              .anyRequest().authenticated();
        });
    return http.build();

  }
}