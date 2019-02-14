package org.beesden.commerce.catalogue.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.commerce.common.model.commerce.CategoryResource;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_category")
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractDomainEntity {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String categoryId;

    @Lob
    private String description;

    @Lob
    private String summary;

    @Column
    private String title;

    /**
     * Convert the DTO to a categoryResource object.
     *
     * @return categoryResource
     */
    public CategoryResource toCategory() {
        CategoryResource categoryResource = new CategoryResource();

        categoryResource.setId(categoryId);
        categoryResource.setTitle(title);
        categoryResource.setSummary(summary);
        categoryResource.setDescription(description);

        return categoryResource;
    }

    /**
     * Update the DTO with data from a categoryResource object.
     *
     * @param categoryResource categoryResource object
     */
    public void update(CategoryResource categoryResource) {
        updateTimestamps("testuser");
        categoryId = categoryResource.getId();
        title = categoryResource.getTitle();
        summary = categoryResource.getSummary();
        description = categoryResource.getDescription();
    }

}