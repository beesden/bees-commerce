package org.beesden.commerce.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private PagedRequest request;
    private List<T> results;
	private Long total;

	public int getTotalPages() {
	    return (int) Math.ceil(total.doubleValue() / request.getResults().doubleValue());
    }

}
