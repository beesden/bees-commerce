package org.beesden.commerce.utils.model;

import org.beesden.commerce.model.entity.Channel;
import org.beesden.commerce.model.entity.Country;
import org.beesden.commerce.model.entity.Currency;
import org.beesden.commerce.model.entity.Language;
import org.beesden.commerce.utils.MasterDataLoader;
import org.springframework.stereotype.Component;

/**
 * Locality information
 *
 * @author tmarson
 *
 */
@Component
public class Locality {

	private Channel channel;
	private Country country;
	private Currency currency;
	private Language language;

	public Channel getChannel() {
		return channel;
	}

	public Country getCountry() {
		return country;
	}

	public Currency getCurrency() {
		return currency;
	}

	public Language getLanguage() {
		return language;
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

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void updateLocality(MasterDataLoader masterDataLoader) {
		this.country = masterDataLoader.getCountry("US");
	}
}