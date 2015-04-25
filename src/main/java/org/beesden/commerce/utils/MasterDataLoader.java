package org.beesden.commerce.utils;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;

import org.beesden.commerce.model.entity.Country;
import org.beesden.commerce.model.entity.Currency;
import org.beesden.commerce.model.entity.Language;
import org.beesden.commerce.service.ChannelService;
import org.beesden.commerce.service.CountryService;
import org.beesden.commerce.service.CurrencyService;
import org.beesden.commerce.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;

@PropertySource("classpath:/app.properties")
public class MasterDataLoader implements ServletContextAware, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ChannelService channelService;

	private Map<String, Country> countryMap;

	@Autowired
	private CountryService countryService;

	private Map<String, Currency> currencyMap;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private Environment env;

	private Map<String, Language> languageMap;

	@Autowired
	private LanguageService languageService;

	public Country getCountry(String countryCode) {
		Country country = null;
		if (countryCode != null) {
			country = countryMap.get(countryCode);
		}

		if (country == null) {
			countryCode = env.getProperty("locality.default.country");
			country = countryMap.get(countryCode);
		}

		return country = countryMap.get(countryCode);
	}

	public Currency getDefaultCurrency() {
		String defaultCurrencyCode = env.getProperty("locality.default.currency");
		return currencyMap.get(defaultCurrencyCode);
	}

	public Language getDefaultLanguage() {
		String defaultLanguageCode = env.getProperty("locality.default.language");
		return languageMap.get(defaultLanguageCode);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		countryMap = countryService.getCountryMap();
		currencyMap = currencyService.getCurrencyMap();
		languageMap = languageService.getLanguageMap();
	}
}