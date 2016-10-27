package org.beesden.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class PagedRequest {

	@Min( 1 )
	private Integer page = 1;

	@Range( max = 60, min = 1 )
	private Integer results = 15;

	private String sort;

	public PagedRequest( int page, int results, String sort ) {
		if ( page > 1 ) {
			this.page = page;
		}
		if ( results > 0 ) {
			this.results = Math.min( results, 60 );
		}
		this.sort = sort;
	}

	public int getStartIndex() {
		return results * ( page - 1 );
	}

	public PageRequest toPageable() {

		if ( sort != null ) {
			return new PageRequest( page - 1, results, new Sort( sort ) );
		} else {
			return new PageRequest( page - 1, results );
		}
	}
}
