package org.beesden.commerce.dao;

import java.util.List;

import org.beesden.commerce.model.entity.Channel;

public interface ChannelDAO {

	public Channel getChannelByIso(String iso);

	public List<Channel> getChannelList();
}