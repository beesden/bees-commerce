package org.beesden.commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.common.model.Product;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode( callSuper = true )
@Table( name = "bees_product" )
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column( nullable = false, unique = true )
	private String productKey;

	@Lob
	private String description;

	@Lob
	private String summary;

	@Column( nullable = false )
	private String title;

	@ManyToMany( fetch = FetchType.LAZY, mappedBy = "products" )
	private Set<CategoryDTO> categories;

	@ElementCollection
	@CollectionTable( name = "bees_product_references", joinColumns = @JoinColumn( name = "product_id" ) )
	private Set<Long> referenceIds;

	//	@OneToMany( fetch = FetchType.EAGER )
	//	private Set<Variant> variants;

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