package org.beesden.commerce.common.model.resource;

import lombok.Data;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.Searchable;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

@Data
public class CategoryResource implements Searchable {

    @NotEmpty
    private String id;
    @NotEmpty
    private String title;
    private String summary;
    private String description;
    private Set<String> parents;

    @Override
    public SearchDocument toSearchDocument() {
        return SearchDocument.builder()
                .title(title)
                .entity(new EntityReference(EntityType.CATEGORY, id))
                .build();
    }

}