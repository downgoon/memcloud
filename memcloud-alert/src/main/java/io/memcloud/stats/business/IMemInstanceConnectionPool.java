package io.memcloud.stats.business;

import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

public interface IMemInstanceConnectionPool {

	/**
	 * 删除客户端
	 * @param host
	 * @param port
	 */
	public void removeClient(String host, int port);
	
	/**
	 * 添加客户端
	 * @param host
	 * @param port
	 */
	public MemcachedClient addClient(String host, int port);
	
	
	public boolean hasClient(String host, int port);
	
	
	public Map<String, MemcachedClient> getConnectionPool();
	
}
