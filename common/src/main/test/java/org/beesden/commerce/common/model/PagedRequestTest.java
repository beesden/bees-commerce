package org.beesden.commerce.common.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

public class PagedRequestTest {

    private PagedRequest request;

    @Before
    public void before() {
        request = new PagedRequest();
    }

    @Test
    public void constructorTest() {

        // Empty arguments
        request = new PagedRequest();
        assertThat(request.getPage()).isEqualTo(1);
        assertThat(request.getResults()).isEqualTo(12);
        assertThat(request.getSort()).isBlank();

        // AllArgs
        request = new PagedRequest(2, 3, "test");
        assertThat(request.getPage()).isEqualTo(2);
        assertThat(request.getResults()).isEqualTo(3);
        assertThat(request.getSort()).isEqualTo("test");

    }

    @Test
    public void getPageTest() {

        // Default set
        assertThat(request.getPage()).isEqualTo(1);

        // Valid set
        request.setPage(5);
        assertThat(request.getPage()).isEqualTo(5);

        // Invalid value
        request.setPage(-5);
        assertThat(request.getPage()).isEqualTo(1);

        // Set from constructor
        request = new PagedRequest(5, 1, null);
        assertThat(request.getPage()).isEqualTo(5);

    }

    @Test
    public void getResultsTest() {

        // Default set
        assertThat(request.getResults()).isEqualTo(12);

        // Valid set
        request.setResults(30);
        assertThat(request.getResults()).isEqualTo(30);

        // Invalid value (too low)
        request.setResults(-5);
        assertThat(request.getResults()).isEqualTo(12);

        // Invalid value (too high)
        request.setResults(65);
        assertThat(request.getResults()).isEqualTo(12);

        // Set from constructor
        request = new PagedRequest(1, 24, null);
        assertThat(request.getResults()).isEqualTo(24);

    }

    @Test
    public void getStartIndexTest() {

        assertThat(request.getStartIndex()).isEqualTo(0);

        request = new PagedRequest(3, 20, null);
        assertThat(request.getStartIndex()).isEqualTo(40);

    }

    @Test
    public void toPageable() {

        Pageable pageable = new PagedRequest().toPageable();
        assertThat(pageable.getOffset()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(12);
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getSort().isUnsorted()).isTrue();

        pageable = new PagedRequest(3, 20, "test").toPageable();
        assertThat(pageable.getOffset()).isEqualTo(40);
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getSort().toString()).isEqualTo("test: ASC");

    }

}
