package org.beesden.commerce.common.model.resource;

import lombok.Data;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.Searchable;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CategoryResource implements Searchable {

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
                .entity(new EntityReference(EntityType.CATEGORY, id))
                .build();
    }

}