package io.memcloud.memdns.dao;

import java.util.LinkedHashMap;
import java.util.List;

import io.memcloud.memdns.dao.entry.AppMemGroup;

public interface IAppMemGroupDao {

	public AppMemGroup getByRecId(Long id);
	
	public List<AppMemGroup> getByAppId(Long appId);
	
	public List<AppMemGroup> getByAppId(List<Long> appIds);
	
	/**
	 * @param	appIds	应用ID列表
	 * @return	各个AppID的分片数量。对于不存在的AppID，分片数量返回NULL，不是0。
	 * */
	public LinkedHashMap<Long,Long> shardCount(List<Long> appIds);
	
	public Long save(AppMemGroup appMemGroup);
	
	public void update(AppMemGroup appMemGroup);
	
	public void delete(Long id);
	
	/** 同一个App的多个分片一次性插入*/
	public void save(List<AppMemGroup> appShards);
	
	/** 根据host:port查询AppMemGroup */
	public AppMemGroup getByHostAndPort(String host, Integer port);
}
