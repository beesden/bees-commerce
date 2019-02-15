package org.beesden.commerce.catalogue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;
import org.beesden.commerce.common.model.commerce.CategoryResource;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_category")
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

    @ManyToMany
    @JoinTable(name = "bees_category_ancestors", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "parent_category_id"))
    private List<Category> ancestors;

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