package io.memcloud.stats.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IMemInstanceDao;
import io.memcloud.memdns.dao.entry.MemInstance;
import io.memcloud.stats.business.IMemInstanceMetaFetcher;

public class MemInstanceMetaFetcher implements IMemInstanceMetaFetcher {

	@Resource( name = "memInstanceDao")
	private IMemInstanceDao memInstanceDao;
	
	@Override
	public List<String> scanMemInstances() {
		List<String> memAddr = new ArrayList<String>();
		List<MemInstance> memInstanceList = memInstanceDao.getAll();
		for (MemInstance instance : memInstanceList)  {
			memAddr.add(instance.getHostIp() + ":" + instance.getPort());
		}
		return memAddr;
	}

}
