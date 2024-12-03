package com.ndurance.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOrigin("https://localhost");  // Web frontend
        corsConfig.addAllowedOrigin("http://localhost");  // Web frontend
        corsConfig.addAllowedOrigin("http://10.0.2.2");        // Android Emulator
        corsConfig.addAllowedOrigin("http://192.168.1.8");     // Local network (Android devices)
        corsConfig.addAllowedOriginPattern("*");              // Allow all for development

        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);  // Apply CORS to all paths

        return new CorsWebFilter(source);
    }


}