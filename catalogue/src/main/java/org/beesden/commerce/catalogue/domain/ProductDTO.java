package org.beesden.commerce.catalogue.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.commerce.common.model.commerce.Product;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_product")
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends AbstractDomainEntity {

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
    private Set<CategoryDTO> categories;

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
    public Product toProduct() {
        Product product = new Product();

        product.setId(productKey);
        product.setTitle(title);
        product.setSummary(summary);
        product.setDescription(description);

        return product;
    }

    /**
     * Update the DTO with data from a product object.
     *
     * @param product product object
     */
    public void update(Product product) {
        productKey = product.getId();
        title = product.getTitle();
        summary = product.getSummary();
        description = product.getDescription();
        updateTimestamps("testuser");
    }
}