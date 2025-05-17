package org.backend.volunteeringbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Allow all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true)
                .exposedHeaders("Authorization", "Content-Disposition")
                .maxAge(3600); // Cache preflight response for 1 hour

    }
}