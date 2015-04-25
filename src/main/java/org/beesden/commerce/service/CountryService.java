package org.beesden.commerce.service;

import java.util.Map;

import org.beesden.commerce.model.entity.Country;

public interface CountryService {

	public Country getCountryByIso(String iso);

	public Map<String, Country> getCountryMap();

}