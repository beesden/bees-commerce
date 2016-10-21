package org.beesden.commerce.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode( callSuper = true )
@Table( name = "bees_category" )
public class Category extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column( nullable = false )
	private String categoryId;

	@Column( columnDefinition = "TEXT" )
	private String description;

	@Column( columnDefinition = "TEXT" )
	private String summary;

	@Column
	private String title;

	@ManyToMany( fetch = FetchType.EAGER )
	private Set<ProductDTO> productDTOs;

}