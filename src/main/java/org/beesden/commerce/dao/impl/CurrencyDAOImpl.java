package org.beesden.commerce.dao.impl;

import java.util.List;

import org.beesden.commerce.dao.CurrencyDAO;
import org.beesden.commerce.model.entity.Currency;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class CurrencyDAOImpl implements CurrencyDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Currency getCurrencyByIso(String iso) {
		Session session = sessionFactory.getCurrentSession();
		return (Currency) session.get(Currency.class, iso);
	}

	@Override
	public List<Currency> getCurrencyList() {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(Currency.class).list();
	}
}