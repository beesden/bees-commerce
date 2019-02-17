package org.beesden.commerce.web.session;

import lombok.Getter;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.CategoryResource;
import org.beesden.commerce.common.model.resource.ProductResource;
import org.beesden.commerce.common.model.resource.SearchResource;
import org.beesden.commerce.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Session {

    private Map<String, CategoryResource> categoryMap = new HashMap<>();
    private String language;
    private String locale;

    @Autowired
    public Session(CategoryClient categoryClient) {

        int page = 0;
        PagedResponse<CategoryResource> response;

        do {
            response = categoryClient.listCategories(page++, 60);
            response.getResults().forEach(category -> categoryMap.put(category.getId(), category));
        } while (response.getTotalPages() > page);

    }
}