package org.beesden.commerce.catalogue.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.commerce.common.model.commerce.ProductResource;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_product")
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractDomainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String productKey;

    @Lob
    private String description;

    @Lob
    private String summary;

    @Column(nullable = false)
    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bees_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @ElementCollection
    @CollectionTable(name = "bees_product_references", joinColumns = @JoinColumn(name = "product_id"))
    private Set<Long> referenceIds;

    //	@OneToMany( fetch = FetchType.EAGER )
    //	private Set<Variant> variants;

    /**
     * Convert the DTO to a product object.
     *
     * @return product
     */
    public ProductResource toProduct() {
        ProductResource productResource = new ProductResource();

        productResource.setId(productKey);
        productResource.setTitle(title);
        productResource.setSummary(summary);
        productResource.setDescription(description);

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
        summary = productResource.getSummary();
        description = productResource.getDescription();
        updateTimestamps("testuser");
    }
}