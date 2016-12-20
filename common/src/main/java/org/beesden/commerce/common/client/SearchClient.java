package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient( "http://search-client" )
public interface SearchClient {

	@RequestMapping( method = RequestMethod.DELETE, value = "/" )
	void clearIndex();

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	SearchResultWrapper performSearch( @Valid @RequestBody SearchForm searchForm );

	@RequestMapping( method = RequestMethod.DELETE, value = "/entities" )
	void removeFromIndex( @Valid @RequestBody EntityReference entity );

	@RequestMapping( method = RequestMethod.POST, value = "/entities" )
	void submitToIndex( @Valid @RequestBody SearchDocument searchDocument );

}