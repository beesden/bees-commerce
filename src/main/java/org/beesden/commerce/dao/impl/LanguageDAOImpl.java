package org.beesden.commerce.dao.impl;

import java.util.List;

import org.beesden.commerce.dao.LanguageDAO;
import org.beesden.commerce.model.entity.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class LanguageDAOImpl implements LanguageDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Language getLanguageByIso(String iso) {
		Session session = sessionFactory.getCurrentSession();
		return (Language) session.get(Language.class, iso);
	}

	@Override
	public List<Language> getLanguageList() {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(Language.class).list();
	}
}