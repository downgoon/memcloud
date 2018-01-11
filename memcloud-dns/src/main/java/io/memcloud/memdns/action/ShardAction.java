package io.memcloud.memdns.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import io.memcloud.cas.IAccount;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.stats.dao.IMemStatDao;
import io.memcloud.stats.model.MemStatSummary;

/**
 * 我的监控->全部实例（把所有应用的分片情况都列表出来）
 * */
public class ShardAction extends BaseAction {

	private static final long serialVersionUID = 8873859556455123754L;
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "memStatDao")
	private IMemStatDao memStatDao;
	
	/**
	 * 前提：登录
	 * 我的监控->全部实例（把所有应用的分片情况都列表出来）
	 * */
	@Override
	public String index() {
		IAccount acc = loginedUser();
		List<AppDesc> apps = appDescDao.getByUid(acc.getUserId());
		if (apps==null || apps.size()<=0) {
			responseModel.setStatus(404);
			responseModel.setMessage("app not found for user "+acc.getUserId());
			return REST("index");
		}
		Map<Long, AppDesc> appMap = new LinkedHashMap<Long, AppDesc>();
		List<Long> appIds = new ArrayList<Long>(apps.size());
		for (AppDesc a : apps) {
			appMap.put(a.getId(), a);
			appIds.add(a.getId());
		}
		List<AppMemGroup> shardList = appMemGroupDao.getByAppId(appIds);
		if (shardList == null || shardList.size() <=0) {
			responseModel.setStatus(405);
			responseModel.setMessage("shards not found for user "+acc.getUserId());
			return REST("index");
		}
		
		Map<String, MemStatSummary> summaryMap = new LinkedHashMap<String, MemStatSummary>();
		for (AppMemGroup shard : shardList) {
			MemStatSummary sm = memStatDao.summary(shard.getMasterIP(), shard.getMasterPort());
			MemStatSummary ss = memStatDao.summary(shard.getSlaveIP(), shard.getSlavePort());
			summaryMap.put(shard.getMasterIP()+":"+shard.getMasterPort(), sm);
			summaryMap.put(shard.getSlaveIP()+":"+shard.getSlavePort(), ss);
		}

		Map<String,Object> attachment = new LinkedHashMap<String, Object>();
		attachment.put("shardList", shardList);
		attachment.put("appMap", appMap);
		attachment.put("summaryMap", summaryMap);
		responseModel.setAttachment(attachment);
		
		return REST("index");
	}

	
	
}
