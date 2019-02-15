package org.beesden.commerce.catalogue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.domain.AbstractDomainEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "bees_currency")
public class Currency extends AbstractDomainEntity {

    @Id
    @Column
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String symbol;

}