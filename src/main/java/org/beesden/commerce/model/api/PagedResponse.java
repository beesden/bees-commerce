package org.beesden.commerce.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class PagedResponse<T> {

	private Collection<T> results;
	private long total;

}
