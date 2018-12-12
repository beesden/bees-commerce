package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient("catalogue-client/category")
public interface CategoryClient {

    @RequestMapping(method = RequestMethod.POST)
    void createCategory(Category category);

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    void deleteCategory(@PathVariable("categoryId") String categoryId);

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    Category getCategory(@PathVariable("categoryId") String categoryId);

    @RequestMapping(method = RequestMethod.GET)
    PagedResponse<Category> listCategories(@Valid PagedRequest pagination);

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
    void updateCategory(@PathVariable("categoryId") String categoryId, Category category);

}