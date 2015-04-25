package org.beesden.commerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.beesden.commerce.dao.ChannelDAO;
import org.beesden.commerce.model.entity.Channel;
import org.beesden.commerce.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelServiceImpl implements ChannelService {

	static final Logger logger = Logger.getLogger(ChannelServiceImpl.class);

	@Autowired
	private ChannelDAO channelDAO;

	@Override
	@Transactional
	public Channel getChannelByIso(String iso) {
		logger.debug("Fetching channel by iso: " + iso);
		return channelDAO.getChannelByIso(iso);
	}

	@Override
	@Transactional
	public Map<String, Channel> getChannelMap() {
		logger.debug("Fetching channel list");
		Map<String, Channel> channelMap = new HashMap<>();
		List<Channel> channelList = channelDAO.getChannelList();
		for (Channel channel : channelList) {
			channelMap.put(channel.getId(), channel);
		}
		return channelMap;
	}

}