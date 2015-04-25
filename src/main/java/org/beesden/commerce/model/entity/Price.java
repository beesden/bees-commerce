package org.beesden.commerce.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.beesden.commerce.utils.constants.TableNames;

@Entity
@Table(name = TableNames.PRICE)
public class Price extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "channel")
	private Channel channel;

	@OneToOne
	@JoinColumn(name = "country")
	private Country country;

	@OneToOne
	@JoinColumn(name = "currency")
	private Currency currency;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	private BigDecimal value;

	public Channel getChannel() {
		return channel;
	}

	public Country getCountry() {
		return country;
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setPrice(BigDecimal value) {
		this.value = value;
	}
}