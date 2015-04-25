package org.beesden.commerce.service;

import java.util.Map;

import org.beesden.commerce.model.entity.Channel;

public interface ChannelService {

	public Channel getChannelByIso(String iso);

	public Map<String, Channel> getChannelMap();

}