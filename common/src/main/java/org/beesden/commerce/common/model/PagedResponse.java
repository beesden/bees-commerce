package org.beesden.commerce.common.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private PagedRequest request;
    private List<T> results;
	private long total;

	public int getTotalPages() {
	    return (int) Math.ceil((double) total / (double) request.getResults());
    }

}
