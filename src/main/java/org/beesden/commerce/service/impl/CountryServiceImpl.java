package org.beesden.commerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.beesden.commerce.dao.CountryDAO;
import org.beesden.commerce.model.entity.Country;
import org.beesden.commerce.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryServiceImpl implements CountryService {

	static final Logger logger = Logger.getLogger(CountryServiceImpl.class);

	@Autowired
	private CountryDAO countryDAO;

	@Override
	@Transactional
	public Country getCountryByIso(String iso) {
		logger.debug("Fetching country by iso: " + iso);
		return countryDAO.getCountryByIso(iso);
	}

	@Override
	@Transactional
	public Map<String, Country> getCountryMap() {
		logger.debug("Fetching country list");
		Map<String, Country> countryMap = new HashMap<>();
		List<Country> countryList = countryDAO.getCountryList();
		for (Country country : countryList) {
			countryMap.put(country.getIso(), country);
		}
		return countryMap;
	}

}