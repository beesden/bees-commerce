package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.resource.SearchDocument;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.SearchResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient("search-client")
public interface SearchClient {

    @RequestMapping(method = RequestMethod.DELETE, value = "/")
    void clearIndex();

    @RequestMapping(method = RequestMethod.POST, value = "/")
    SearchResource performSearch(@Valid @RequestBody SearchRequest searchRequest);

    @RequestMapping(method = RequestMethod.DELETE, value = "/results")
    void removeFromIndex(@Valid @RequestBody EntityReference entity);

    @RequestMapping(method = RequestMethod.POST, value = "/results")
    void submitToIndex(@Valid @RequestBody SearchDocument searchDocument);

}