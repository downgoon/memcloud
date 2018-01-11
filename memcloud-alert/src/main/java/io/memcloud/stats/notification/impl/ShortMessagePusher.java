package io.memcloud.stats.notification.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShortMessagePusher {

	private static final Logger LOG = LoggerFactory.getLogger(ShortMessagePusher.class);

	private String smsGateway = "http://10.15.2.57/ppp/sns.php?key=x1@9eng&dest=%s&fee=%s&mess=%s";

	public void sendShortMessage(List<String> mobiles, String message) {
		if (mobiles == null || mobiles.size() == 0) {
			return;
		}

		for (String mobile : mobiles) {
			notifyOne(mobile, message);
		}
	}

	protected void notifyOne(String mobile, String message) {
		String smsUrl = String.format(smsGateway, mobile, mobile, message);
		LOG.info("sending mobile notify  : mobile {}, message {} ", mobile, message);
		HttpClientHandler.notifyGetPage(smsUrl);
		LOG.info("send ok mobile notify : mobile {}, message {} ", mobile, message);
	}

}
