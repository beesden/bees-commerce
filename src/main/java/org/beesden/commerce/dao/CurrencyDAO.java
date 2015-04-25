package org.beesden.commerce.dao;

import java.util.List;

import org.beesden.commerce.model.entity.Currency;

public interface CurrencyDAO {

	public Currency getCurrencyByIso(String iso);

	public List<Currency> getCurrencyList();
}