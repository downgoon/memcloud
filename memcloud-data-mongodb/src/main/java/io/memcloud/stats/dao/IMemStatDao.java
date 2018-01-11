package io.memcloud.stats.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import io.memcloud.stats.model.MemStatSummary;

public interface IMemStatDao {

	/**
	 * @param	memIP	Memcached实例的IP地址
	 * @param	memPort	Memcached实例的端口地址
	 * @param	statDate	统计日期（忽略HH:mm:SS）
	 * @return	y=f(x) 函数离散值，其中：x表示时间HH:mm；y表示GET数量。
	 * */
	public LinkedHashMap<String, Long> trendGet(String memIP, int memPort, Date statDate);
	
	public LinkedHashMap<String, Long> trendSet(String memIP, int memPort, Date statDate);
	
	public LinkedHashMap<String, Long> trendHit(String memIP, int memPort, Date statDate);
	
	public LinkedHashMap<String, Long> trendMis(String memIP, int memPort, Date statDate);
	
	/**
	 * @param cmdType {0:GET,1:SET,2:Hit,3:Mis}
	 * */
	public LinkedHashMap<String, Long> trendCmd(String memIP, int memPort,Date statDate, int cmdType);
	
	public MemStatSummary summary(String memIP, int memPort);
	
}
