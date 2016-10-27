package org.beesden.common.service;

import org.beesden.common.model.EntityReference;
import org.beesden.common.model.search.SearchDocument;
import org.beesden.common.model.search.SearchForm;
import org.beesden.common.model.search.SearchResultWrapper;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@FeignClient( name = "search-client", url = "http://search-client" )
public interface SearchClient {

	@DeleteMapping( value = "/" )
	void clearIndex();

	@GetMapping( value = "/" )
	SearchResultWrapper performSearch( @Valid @RequestBody SearchForm searchForm );

	@DeleteMapping( value = "result" )
	void removeFromIndex( @Valid @RequestBody EntityReference entity );

	@PostMapping( value = "result" )
	void submitToIndex( @Valid @RequestBody SearchDocument searchDocument );

}