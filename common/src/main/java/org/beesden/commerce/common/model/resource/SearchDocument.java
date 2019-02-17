package org.beesden.commerce.common.model.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beesden.commerce.common.model.EntityReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDocument {

    private EntityReference entity;
    private String title;
    private Map<String, Set<String>> facets = new HashMap<>();

}

