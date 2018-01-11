package io.memcloud.stats.notification;

import java.util.List;

public interface IFaultNotify {

	public void notifyFailure(String email, List<String> mobiles, FaultEvent event);
	
	public void notifyRecovery(String email, List<String> mobiles, FaultEvent event);
	
}
