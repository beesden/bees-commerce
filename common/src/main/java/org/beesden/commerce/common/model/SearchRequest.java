package org.beesden.commerce.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRequest extends PagedRequest {

    private String term;
    private Set<String> ids;
    private Set<EntityType> types;
    private Set<String> facets = new HashSet<>();

    public SearchSortField getSortField() {

        if (super.getSort() != null) {

            switch (super.getSort()) {
                case "title":
                    return SearchSortField.TITLE;
                case "date":
                case "updated":
                    return SearchSortField.DATE;
                case "price":
                    return SearchSortField.VALUE;
            }
        }

        return SearchSortField.DEFAULT;
    }

    @AllArgsConstructor
    public enum SearchSortField {
        DEFAULT(""),
        TITLE("byTitle"),
        DATE("byDate"),
        VALUE("byValue");

        @Getter
        private String value;
    }

}

