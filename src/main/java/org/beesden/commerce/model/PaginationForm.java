package org.beesden.commerce.model;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

public class PaginationForm {

	private Integer max;

	@Min(0)
	private Integer min;

	@Min(0)
	private Integer page;

	@Range(max = 60, min = 1)
	private Integer results;

	public Integer getMax() {
		return max;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getResults() {
		return results;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setResults(Integer results) {
		this.results = results;
	}
}
