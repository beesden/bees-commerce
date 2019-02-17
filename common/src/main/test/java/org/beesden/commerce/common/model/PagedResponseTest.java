package org.beesden.commerce.common.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PagedResponseTest {

    private PagedResponse<String> response;

    @Before
    public void before() {
        PagedRequest request = new PagedRequest(3, 15, null);
        response = new PagedResponse<>(request, Collections.emptyList(), 0L);
    }

    @Test
    public void getTotalPages() {

        PagedRequest request = response.getRequest();

        assertThat(response.getTotalPages()).isEqualTo(0);

        response.setTotal(100L);
        assertThat(response.getTotalPages()).isEqualTo(7);

        request.setResults(20);
        assertThat(response.getTotalPages()).isEqualTo(5);

    }

}
