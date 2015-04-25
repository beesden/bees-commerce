package org.beesden.commerce.service;

import java.util.Map;

import org.beesden.commerce.model.entity.Currency;

public interface CurrencyService {

	public Currency getCurrencyByIso(String iso);

	public Map<String, Currency> getCurrencyMap();

}