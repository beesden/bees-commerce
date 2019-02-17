package org.beesden.commerce.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.beesden.commerce.common.model.resource.CategoryResource;
import org.beesden.commerce.common.model.resource.SearchResource;

@Data
@AllArgsConstructor
public class CategoryResults {

    public CategoryResource category;
    public SearchResource products;

}