package org.beesden.commerce.catalogue.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.common.domain.AbstractDomainEntity;
import org.beesden.common.model.Category;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode( callSuper = true )
@Table( name = "bees_category" )
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends AbstractDomainEntity {

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column( nullable = false )
	private String categoryId;

	@Lob
	private String description;

	@Lob
	private String summary;

	@Column
	private String title;

	@ManyToMany( fetch = FetchType.LAZY )
	@JoinTable( name = "bees_product_category",
			joinColumns = @JoinColumn( name = "product_id" ),
			inverseJoinColumns = @JoinColumn( name = "category_id" ) )
	private Set<ProductDTO> products;

	/**
	 * Convert the DTO to a category object.
	 *
	 * @return category
	 */
	public Category toCategory() {
		Category category = new Category();

		category.setId( categoryId );
		category.setTitle( title );
		category.setSummary( summary );
		category.setDescription( description );

		return category;
	}

	/**
	 * Update the DTO with data from a category object.
	 *
	 * @param category category object
	 */
	public void update( Category category ) {
		categoryId = category.getId();
		title = category.getTitle();
		summary = category.getSummary();
		description = category.getDescription();
	}

}