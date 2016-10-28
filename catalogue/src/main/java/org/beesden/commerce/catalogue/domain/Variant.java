package org.beesden.commerce.catalogue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.common.domain.AbstractDomainEntity;

import javax.persistence.*;
import java.util.Set;

// @Entity
@Data
@EqualsAndHashCode( callSuper = true )
// @Table( name = "bees_variant" )
public class Variant extends AbstractDomainEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column
	private String name;

	@OneToMany( fetch = FetchType.EAGER )
	@JoinColumn( name = "variantId", nullable = false )
	private Set<Price> prices;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "productId", nullable = false )
	private ProductDTO productDTO;

	@Column
	private String sku;

}