package org.beesden.commerce.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

// @Entity
@Data
@EqualsAndHashCode( callSuper = true )
// @Table( name = "bees_product_price" )
public class Price extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue
	private long id;

	@Column
	private BigDecimal value;
}