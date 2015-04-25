package org.beesden.commerce.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.beesden.commerce.utils.constants.TableNames;

@Entity
@Table(name = TableNames.CURRENCY)
public class Currency extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@Size(min = 3, max = 3)
	private String iso;

	@Column
	private String name;

	@Column
	private String symbol;

	public Boolean equals(Currency currency) {
		return iso.equals(currency.getIso());
	}

	public String getIso() {
		return iso;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}