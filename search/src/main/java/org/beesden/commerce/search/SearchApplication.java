package org.beesden.commerce.search;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;

@EnableDiscoveryClient
@SpringBootApplication
public class SearchApplication {

	public static void main( String[] args ) {
		SpringApplication.run( SearchApplication.class, args );
	}

	@Bean( name = "taxonomy" )
	@Profile( { "!test" } )
	public Directory facetDirectory() throws IOException {
		return FSDirectory.open( new File( "/lucene/test" ).toPath() );
	}

	@Bean( name = "index" )
	@Profile( { "!test" } )
	public Directory indexDirectory() throws IOException {
		return FSDirectory.open( new File( "/lucene" ).toPath() );
	}

	@Bean( name = "taxonomy" )
	@Profile( { "test" } )
	public Directory testFacetDirectory() throws IOException {
		return new RAMDirectory();
	}

	@Bean( name = "index" )
	@Profile( { "test" } )
	public Directory testIndexDirectory() throws IOException {
		return new RAMDirectory();
	}

}