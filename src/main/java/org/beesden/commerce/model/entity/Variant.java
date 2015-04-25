package org.beesden.commerce.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.beesden.commerce.utils.constants.TableNames;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = TableNames.VARIANT)
public class Variant extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column
	private String name;

	@Column
	private Integer order;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "variantId", nullable = false)
	private Set<Price> prices;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	private Product product;

	@Id
	@Column
	private String sku;

	public String getName() {
		return name;
	}

	public Integer getOrder() {
		return order;
	}

	public Set<Price> getPrices() {
		return prices;
	}

	public Product getProduct() {
		return product;
	}

	public String getSku() {
		return sku;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public void setPrices(Set<Price> prices) {
		this.prices = prices;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}