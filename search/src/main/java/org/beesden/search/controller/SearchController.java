package org.beesden.search.controller;

import org.beesden.common.EntityReference;
import org.beesden.common.model.SearchDocument;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResultWrapper;
import org.beesden.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( value = "/" )
public class SearchController {

	@Autowired
	private SearchService searchService;

	@RequestMapping( method = RequestMethod.GET )
	public SearchResultWrapper performSearch( @Valid SearchForm searchForm ) {
		return searchService.performSearch( searchForm );
	}

	@RequestMapping( method = RequestMethod.DELETE )
	@ResponseStatus( HttpStatus.NO_CONTENT )
	public void removeFromIndex( @Valid @RequestBody EntityReference entity ) {
		searchService.removeFromIndex( entity );
	}

	@RequestMapping( method = RequestMethod.POST )
	@ResponseStatus( HttpStatus.CREATED )
	public void submitToIndex( @Valid @RequestBody SearchDocument searchDocument ) {
		searchService.submitToIndex( searchDocument );
	}

}