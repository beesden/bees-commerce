package org.beesden.commerce.service;

import java.util.Map;

import org.beesden.commerce.model.entity.Language;

public interface LanguageService {

	public Language getLanguageByIso(String iso);

	public Map<String, Language> getLanguageMap();

}