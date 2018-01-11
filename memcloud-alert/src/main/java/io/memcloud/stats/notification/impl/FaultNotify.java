package io.memcloud.stats.notification.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.memcloud.stats.notification.FaultEvent;
import io.memcloud.stats.notification.IFaultNotify;

public class FaultNotify implements IFaultNotify {
	
	private static final Logger LOG = LoggerFactory.getLogger(FaultNotify.class);
	
	static final String failureTemplate = "memcloud:%s:%s,http://memcloud.io/memcloud/fault/%s";
	
	static final String recoveryTemplate = "memcloud:%s:%s,OK";

	private ShortMessagePusher shortMessagePusher = new ShortMessagePusher();
	
	@Override
	public void notifyFailure(String email, List<String> mobiles, FaultEvent event) {
		String message = String.format(failureTemplate, event.getHost(), event.getPort(), event.getId());
		notifyMessage(email, mobiles, message);
	}
	
	
	@Override
	public void notifyRecovery(String email, List<String> mobiles, FaultEvent event) {
		String message = String.format(recoveryTemplate, event.getHost(), event.getPort());
		notifyMessage(email, mobiles, message);
	}
	
	
	protected void notifyMessage(String email, List<String> mobiles, String message) {
		LOG.info("fault notify: {} {}", email, message);
		shortMessagePusher.sendShortMessage(mobiles, message);
		EmailPusher.getInstance().sendEmail(email, message);
	}
}
