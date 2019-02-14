package org.beesden.commerce.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.beesden.commerce.common.model.commerce.CategoryResource;
import org.beesden.commerce.common.model.search.SearchResultWrapper;

@Data
@AllArgsConstructor
public class CategoryResults {

    public CategoryResource category;
    public SearchResultWrapper products;

}