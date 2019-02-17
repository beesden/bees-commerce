package org.beesden.commerce.catalogue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;
import org.beesden.commerce.common.model.resource.ProductResource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_product_variant")
public class ProductVariant extends AbstractDomainEntity {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column
    private String title;

    @Lob
    private String description;

    @Column
    private String sku;

    @Column
    private String size;

    @Column
    private String colour;

    @Column
    private LocalDateTime availableFrom;

    @Column
    private LocalDateTime availableTo;

    @Column
    private LocalDateTime availablePreOrder;

    @Column
    private LocalDateTime availableBackOrder;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "variantId", nullable = false)
    private Set<ProductPrice> prices;

    /**
     * Convert the DTO to a product object.
     *
     * @return product
     */
    public ProductResource.Variant toResource() {
        ProductResource.Variant resource = new ProductResource.Variant();

        resource.setTitle(title);
        resource.setSku(sku);
        resource.setDescription(description);
        resource.setColour(colour);
        resource.setSize(size);
        resource.setPrice(new BigDecimal(25));

        return resource;
    }

    /**
     * Update the DTO with data from a productResource object.
     *
     * @param resource productResource object
     */
    public void update(ProductResource.Variant resource) {
        sku = resource.getSku();
        title = resource.getTitle();
        description = resource.getDescription();
        updateTimestamps("testuser");
    }

}