package org.beesden.commerce.common.model.commerce;

import lombok.Data;
import org.beesden.commerce.common.model.Searchable;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Product implements Searchable {

    @NotEmpty
    private String id;
    @NotEmpty
    private String title;
    private String summary;
    private String description;

    @Override
    public SearchDocument toSearchDocument() {
        return SearchDocument.builder()
                .title(title)
                .build();
    }
}