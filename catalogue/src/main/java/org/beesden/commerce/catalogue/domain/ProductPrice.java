package org.beesden.commerce.catalogue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_product_price")
public class ProductPrice extends AbstractDomainEntity {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column
    private BigDecimal value;

    @Column
    @OrderBy
    private LocalDateTime start;

    @Column
    private LocalDateTime end;

    // TODO
    @Column
    private String locale;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}