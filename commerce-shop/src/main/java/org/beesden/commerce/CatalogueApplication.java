package org.beesden.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class CatalogueApplication {

	public static void main( String[] args ) {
		SpringApplication.run( CatalogueApplication.class, args );
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings( CorsRegistry registry ) {
				registry.addMapping( "/**" ).allowedOrigins( "*" );
			}
		};
	}

}