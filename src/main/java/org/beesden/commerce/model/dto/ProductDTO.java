package org.beesden.commerce.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.model.api.Product;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode( callSuper = true )
@Table( name = "bees_product" )
public class ProductDTO extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column( nullable = false )
	private String productKey;

	@Column( columnDefinition = "TEXT" )
	private String description;

	@Column( columnDefinition = "TEXT" )
	private String summary;

	@Column( nullable = false )
	private String title;

	@OneToMany( fetch = FetchType.EAGER )
	private Set<Variant> variants;

	public void update( Product product ) {
		productKey = product.getId();
		title = product.getTitle();
		summary = product.getSummary();
		description = product.getDescription();
	}
}