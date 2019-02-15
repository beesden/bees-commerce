package org.beesden.commerce.catalogue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;
import org.beesden.commerce.common.model.commerce.ProductResource;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_product")
public class Product extends AbstractDomainEntity {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String productKey;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String summary;

    @Lob
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private List<ProductVariant> variants;

    @ManyToMany
    @JoinTable(name = "bees_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    /**
     * Convert the DTO to a product object.
     *
     * @return product
     */
    public ProductResource toResource() {
        ProductResource productResource = new ProductResource();

        productResource.setId(productKey);
        productResource.setTitle(title);
        productResource.setDescription(description);
        productResource.setCategories(getCategories().stream().map(Category::getCategoryId).collect(Collectors.toSet()));

        if (variants != null) {
            productResource.setVariants(variants.stream().map(ProductVariant::toResource).collect(Collectors.toList()));
        }

        return productResource;
    }

    /**
     * Update the DTO with data from a productResource object.
     *
     * @param productResource productResource object
     */
    public void update(ProductResource productResource) {
        productKey = productResource.getId();
        title = productResource.getTitle();
        description = productResource.getDescription();
        updateTimestamps("testuser");
    }
}