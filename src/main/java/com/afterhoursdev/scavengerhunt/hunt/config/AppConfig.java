package com.afterhoursdev.scavengerhunt.hunt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * The AppConfig class contains the Bean definitions for the Hunt Service application. Beans
 * defined in a @Configuration class are created and managed by the Spring IoC container when 
 * the application context is being initialized. Beans can be injected into other beans via 
 * constructor, setter, or field injection. 
 *   
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Configuration
public class AppConfig {
       
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				     .allowedMethods(CorsConfiguration.ALL)
                     .allowedHeaders(CorsConfiguration.ALL)
                     .allowedOriginPatterns(CorsConfiguration.ALL);
			}
		};
	}
}