package io.memcloud.memdns.business;

import io.memcloud.memdns.dao.entry.AppConf;
import io.memcloud.memdns.dao.entry.AppConfAudit;
import io.memcloud.memdns.dao.entry.AppDesc;

public interface IMemDNSPublisher {

	/** 发布某个应用的最新配置 */
	public AppConfAudit publishAppConf(AppDesc appDesc, long userId);
	
//	public AppConfAudit publishAppConf(long appId, long userId);
	
	
	public AppConf getAppConf(long appId);
}
