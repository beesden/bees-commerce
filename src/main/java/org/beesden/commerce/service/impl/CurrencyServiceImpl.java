package org.beesden.commerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.beesden.commerce.dao.CurrencyDAO;
import org.beesden.commerce.model.entity.Currency;
import org.beesden.commerce.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

	static final Logger logger = Logger.getLogger(CurrencyServiceImpl.class);

	@Autowired
	private CurrencyDAO currencyDAO;

	@Override
	@Transactional
	public Currency getCurrencyByIso(String iso) {
		logger.debug("Fetching currency by iso: " + iso);
		return currencyDAO.getCurrencyByIso(iso);
	}

	@Override
	@Transactional
	public Map<String, Currency> getCurrencyMap() {
		logger.debug("Fetching currency list");
		Map<String, Currency> currencyMap = new HashMap<>();
		List<Currency> currencyList = currencyDAO.getCurrencyList();
		for (Currency currency : currencyList) {
			currencyMap.put(currency.getIso(), currency);
		}
		return currencyMap;
	}

}