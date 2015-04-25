package org.beesden.commerce.dao;

import java.util.List;

import org.beesden.commerce.model.entity.Country;

public interface CountryDAO {

	public Country getCountryByIso(String iso);

	public List<Country> getCountryList();
}