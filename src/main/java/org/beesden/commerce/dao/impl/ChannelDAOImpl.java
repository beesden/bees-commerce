package org.beesden.commerce.dao.impl;

import java.util.List;

import org.beesden.commerce.dao.ChannelDAO;
import org.beesden.commerce.model.entity.Channel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class ChannelDAOImpl implements ChannelDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Channel getChannelByIso(String iso) {
		Session session = sessionFactory.getCurrentSession();
		return (Channel) session.get(Channel.class, iso);
	}

	@Override
	public List<Channel> getChannelList() {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(Channel.class).list();
	}
}