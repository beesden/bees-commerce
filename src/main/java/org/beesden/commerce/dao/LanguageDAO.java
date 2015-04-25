package org.beesden.commerce.dao;

import java.util.List;

import org.beesden.commerce.model.entity.Language;

public interface LanguageDAO {

	public Language getLanguageByIso(String iso);

	public List<Language> getLanguageList();
}