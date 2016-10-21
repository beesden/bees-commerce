package org.beesden.commerce.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.common.model.Product;

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

	@Column( nullable = false, unique = true )
	private String productKey;

	@Column( columnDefinition = "TEXT" )
	private String description;

	@Column( columnDefinition = "TEXT" )
	private String summary;

	@Column( nullable = false )
	private String title;

	@OneToMany( fetch = FetchType.EAGER )
	private Set<Variant> variants;

	public Product toProduct() {
		Product product = new Product();

		product.setId( productKey );
		product.setTitle( title );
		product.setSummary( summary );
		product.setDescription( description );

		return product;
	}

	public void update( Product product ) {
		productKey = product.getId();
		title = product.getTitle();
		summary = product.getSummary();
		description = product.getDescription();
	}
}