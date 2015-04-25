package org.beesden.commerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.beesden.commerce.dao.LanguageDAO;
import org.beesden.commerce.model.entity.Language;
import org.beesden.commerce.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LanguageServiceImpl implements LanguageService {

	static final Logger logger = Logger.getLogger(LanguageServiceImpl.class);

	@Autowired
	private LanguageDAO languageDAO;

	@Override
	@Transactional
	public Language getLanguageByIso(String iso) {
		logger.debug("Fetching language by iso: " + iso);
		return languageDAO.getLanguageByIso(iso);
	}

	@Override
	@Transactional
	public Map<String, Language> getLanguageMap() {
		logger.debug("Fetching language list");
		Map<String, Language> languageMap = new HashMap<>();
		List<Language> languageList = languageDAO.getLanguageList();
		for (Language language : languageList) {
			languageMap.put(language.getIso(), language);
		}
		return languageMap;
	}

}