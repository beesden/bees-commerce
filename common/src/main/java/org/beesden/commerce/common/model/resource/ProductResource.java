package org.beesden.commerce.common.model.resource;

import lombok.Data;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.Searchable;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class ProductResource implements Searchable {

    @NotEmpty
    private String id;
    @NotEmpty
    private String title;
    private String description;
    private List<Variant> variants = new ArrayList<>();
    private Set<String> categories = new HashSet<>();

    @Data
    public static class Variant {

        @NotEmpty
        private String sku;
        @NotEmpty
        private String title;
        private String description;
        private String colour;
        private String size;
        private BigDecimal price;

    }

    public List<String> getColours() {
        return variants.stream().map(Variant::getColour).distinct().sorted().collect(Collectors.toList());
    }

    public double getMinPrice() {
        return variants.stream().map(Variant::getPrice).sorted().findFirst().orElse(BigDecimal.ZERO).doubleValue();
    }

    public double getMaxPrice() {
        return variants.stream().map(Variant::getPrice).sorted().findFirst().orElse(BigDecimal.ZERO).doubleValue();
    }

    @Override
    public SearchDocument toSearchDocument() {

        Map<String, Set<String>> facets = new HashMap<>();
        facets.put("Colour", variants.stream().map(Variant::getColour).filter(Objects::nonNull).collect(Collectors.toSet()));
        facets.put("Size", variants.stream().map(Variant::getSize).filter(Objects::nonNull).collect(Collectors.toSet()));
        facets.put("category", categories);


        return SearchDocument.builder()
                .title(title)
                .value(getMinPrice())
                .facets(facets)
                .entity(new EntityReference(EntityType.PRODUCT, id))
                .build();
    }
}