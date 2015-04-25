package org.beesden.commerce.dao.impl;

import java.util.List;

import org.beesden.commerce.dao.CountryDAO;
import org.beesden.commerce.model.entity.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class CountryDAOImpl implements CountryDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Country getCountryByIso(String iso) {
		Session session = sessionFactory.getCurrentSession();
		return (Country) session.get(Country.class, iso);
	}

	@Override
	public List<Country> getCountryList() {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(Country.class).list();
	}
}