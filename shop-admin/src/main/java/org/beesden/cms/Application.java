package org.beesden.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

	@Autowired
	private ResourceProperties resourceProperties = new ResourceProperties();

	public static void main( String[] args ) {
		SpringApplication.run( Application.class, args );
	}

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry ) {
		Integer cachePeriod = resourceProperties.getCachePeriod();

		registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
		registry.addResourceHandler( "/resources/**" )
				.addResourceLocations( "static/" )
				.addResourceLocations( "classpath:/static/" )
				.setCachePeriod( cachePeriod );

	}

}