package org.beesden.search.model;

import lombok.Data;
import org.beesden.common.model.PagedRequest;

@Data
public class SearchForm extends PagedRequest {

	private String term;
	private String[] id;
	private String[] type;
	private String[] facet;

}