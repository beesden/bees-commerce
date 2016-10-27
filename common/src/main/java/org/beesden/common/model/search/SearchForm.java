/**
 * Autogenerated by Thrift Compiler (0.9.1)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *
 * @generated
 */
package org.beesden.common.model.search;

import lombok.Data;
import org.beesden.common.model.EntityType;
import org.beesden.common.model.PagedRequest;

import java.util.Set;

@Data
public class SearchForm extends PagedRequest {

	private String term;
	private Set<String> ids;
	private Set<EntityType> types;
	private Set<String> facets;

}
