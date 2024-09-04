package com.ndurance.order_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableMethodSecurity(securedEnabled=true, prePostEnabled=true)
@EnableWebSecurity
@Configuration
public class WebSecurity{

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        
     http
        .csrf((csrf) -> csrf.disable())
         .authorizeHttpRequests((authz) -> authz
        .requestMatchers(HttpMethod.GET, SecurityConstants.PRODUCTS)
        .permitAll().
                 requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
        .permitAll().requestMatchers(HttpMethod.GET, SecurityConstants.PRODUCTS)
                 .permitAll()
        .anyRequest().authenticated())
     
        .addFilter(new AuthorizationFilter(authenticationManager))
        .authenticationManager(authenticationManager)
        .sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
         http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
    	final CorsConfiguration configuration = new CorsConfiguration();
    	   
    	configuration.setAllowedOrigins(Arrays.asList("*"));
    	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
    	configuration.setAllowCredentials(true);
    	configuration.setAllowedHeaders(Arrays.asList("*"));
    	
    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	
    	return source;
    }
}
