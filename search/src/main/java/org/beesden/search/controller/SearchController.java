package org.beesden.search.controller;

import org.beesden.common.model.SearchDocument;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResult;
import org.beesden.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SearchController {

	@Autowired
	private SearchService searchService;

	@RequestMapping( method = RequestMethod.GET )
	public SearchResult performSearch( @Valid SearchForm searchForm ) {
		return searchService.performSearch( searchForm );
	}

	@RequestMapping( value = "/{indexId}", method = RequestMethod.DELETE )
	public void removeFromIndex( @PathVariable String indexId, @Valid @RequestBody SearchDocument searchDocument ) {
		searchService.removeFromIndex( searchDocument );
	}

	@RequestMapping( method = RequestMethod.POST )
	public void submitToIndex( @Valid @RequestBody SearchDocument searchDocument ) {
		searchService.submitToIndex( searchDocument );
	}

}