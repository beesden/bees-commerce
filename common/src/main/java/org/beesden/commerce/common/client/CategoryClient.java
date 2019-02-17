package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.resource.CategoryResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("catalogue-client/category")
public interface CategoryClient {

    @RequestMapping(method = RequestMethod.POST)
    void createCategory(@RequestBody CategoryResource categoryResource);

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    void deleteCategory(@PathVariable("categoryId") String categoryId);

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    CategoryResource getCategory(@PathVariable("categoryId") String categoryId);

    @RequestMapping(method = RequestMethod.GET)
    PagedResponse<CategoryResource> listCategories(@RequestParam("page") int page, @RequestParam("results") int results);

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
    void updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody CategoryResource categoryResource);

}