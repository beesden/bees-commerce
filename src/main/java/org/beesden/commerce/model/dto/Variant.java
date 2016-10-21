package org.beesden.commerce.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode( callSuper = true )
@Table( name = "bees_product_variant" )
public class Variant extends AbstractEntity {

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

	@JsonIgnore
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "productId", nullable = false )
	private ProductDTO productDTO;

	@Column
	private String sku;

}