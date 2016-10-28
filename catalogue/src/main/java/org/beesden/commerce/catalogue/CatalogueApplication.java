package org.beesden.commerce.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients( basePackages = { "org.beesden.commerce" } )
public class CatalogueApplication {
	public static void main( String[] args ) {
		SpringApplication.run( CatalogueApplication.class, args );
	}
}

//@Controller
//class CatalogueController {
//
//	@Autowired
//	private SearchClient searchClient;
//
//	@RequestMapping( "/search" )
//	@ResponseBody
//	public SearchResultWrapper contributors() {
//		return searchClient.performSearch( new SearchForm() );
//	}
//
//}

