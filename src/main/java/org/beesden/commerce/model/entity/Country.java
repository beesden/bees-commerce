package org.beesden.commerce.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.beesden.commerce.utils.constants.TableNames;

@Entity
@Table(name = TableNames.COUNTRY)
public class Country extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private String iso;

	@Column
	private String name;

	@Column
	private Integer order;

	public Boolean equals(Country country) {
		return iso.equals(country.getIso());
	}

	public String getIso() {
		return iso;
	}

	public String getName() {
		return name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}