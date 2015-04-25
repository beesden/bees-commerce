package org.beesden.commerce.model.entity;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.beesden.commerce.utils.constants.TableNames;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name = TableNames.PRODUCT)
public class Product extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private String id;

	@Column(columnDefinition = "TEXT")
	private String longDescription;

	@Column(columnDefinition = "TEXT")
	private String shortDescription;

	@Column
	private String title;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
	@OrderBy(clause = "order")
	@MapKey
	private Map<String, Variant> variants;

	public String getId() {
		return id;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getTitle() {
		return title;
	}

	public Map<String, Variant> getVariants() {
		return variants;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVariants(Map<String, Variant> variants) {
		this.variants = variants;
	}
}