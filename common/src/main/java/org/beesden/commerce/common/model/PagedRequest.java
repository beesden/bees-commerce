package org.beesden.commerce.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
public class PagedRequest {

    private static int DEFAULT_RESULTS = 12;
    private static int MAX_RESULTS = 60;

    @Getter
    private int page = 1;

    @Getter
    private int results = 12;

    @Getter
    @Setter
    private String sort;

    public PagedRequest(int page, int results, String sort) {
        setPage(page);
        setResults(results);
        this.sort = sort;
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    public void setResults(int results) {
        this.results = (results > MAX_RESULTS || results < 1) ? DEFAULT_RESULTS : results;
    }

    public int getStartIndex() {
        return results * (page - 1);
    }
    public int getEndIndex() {
        return results * page;
    }

    public PageRequest toPageable() {

        if (sort != null) {
            return PageRequest.of(page - 1, results, Sort.by(sort));
        } else {
            return PageRequest.of(page - 1, results);
        }
    }

}
