package org.beesden.commerce.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.commerce.Category;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Data
@AllArgsConstructor
public class APICategory {

    public Category data;
    public SearchResultWrapper products;

}