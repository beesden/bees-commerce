package org.beesden.search;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Application {

	public static void main( String[] args ) {
		SpringApplication.run( Application.class, args );
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

	@Bean
	@Profile( { "!test" } )
	public Directory fsDirectory() throws IOException {
		return FSDirectory.open( new File( "/lucene" ).toPath() );
	}

	@Bean
	@Profile( { "test" } )
	public Directory ramDirectory() throws IOException {
		return new RAMDirectory();
	}

}