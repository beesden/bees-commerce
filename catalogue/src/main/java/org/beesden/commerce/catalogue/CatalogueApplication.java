package org.beesden.commerce.catalogue;

import org.beesden.common.client.SearchClient;
import org.beesden.common.model.search.SearchForm;
import org.beesden.common.model.search.SearchResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients( basePackages = { "org.beesden", "example" } )
public class CatalogueApplication {
	public static void main( String[] args ) {
		SpringApplication.run( CatalogueApplication.class, args );
	}
}

@Controller
class CatalogueController {

	@Autowired
	private SearchClient searchClient;

	@RequestMapping( "/search" )
	@ResponseBody
	public SearchResultWrapper contributors() {
		return searchClient.performSearch( new SearchForm() );
	}

}

