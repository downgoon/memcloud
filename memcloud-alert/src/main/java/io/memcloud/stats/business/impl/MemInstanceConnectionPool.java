package io.memcloud.stats.business.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import io.memcloud.stats.business.IMemInstanceConnectionPool;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * @author ganghuawang
 * 初始化memcached客户端，并监听状态
 * @deprecated
 * @see	ConcurrentMemInstanceConnectionPool
 */
public class MemInstanceConnectionPool implements IMemInstanceConnectionPool {

	private static Logger LOG = Logger.getLogger(MemInstanceConnectionPool.class);
	
	
	private Map<String, MemcachedClient> clientPool = new ConcurrentHashMap<String,MemcachedClient>();

	@Override
	public Map<String, MemcachedClient> getConnectionPool() {
		return clientPool;
	}
	
	@Override
	public MemcachedClient addClient(String host, int port) {
		MemcachedClient client = createClient(host, port);
		LOG.info("xmemcached client list size :" + clientPool.size());
		return client;
	}

	@Override
	public void removeClient(String host, int port) {
		MemcachedClient client = clientPool.get(host+":"+port);
		try {
			if(client != null) {
				client.shutdown();
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			clientPool.remove(host+":"+port);
		}
		LOG.info("xmemcached client list size :" + clientPool.size());
	}
	
	
	@Override
	public boolean hasClient(String host, int port) {
		return clientPool.containsKey(host+":"+port);
	}

	/**
	 * 创建xmemcached客户端连接
	 * @param host
	 * @param port
	 */
	private MemcachedClient createClient(String host, int port){
		// 如果client已经存在，不处理
		MemcachedClient found = clientPool.get(host+":"+port);
		if(found != null) {
			return found;
		}

		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(host+ ":" + port));
		try {

			// client参数优化
			builder.setConnectionPoolSize(1);
			builder.getConfiguration().setSessionIdleTimeout(20000); // 设为如果连接超时没有任何IO操作就发起心跳检测，默认5秒
			builder.getConfiguration().setStatisticsServer(false);	// 关闭客户端统计

			// 创建client
			MemcachedClient client = builder.build();
			
			
			// 改为后期加入呢 ？？ liwei
			// client.addStateListener(new MemInstanceMonitor());
			
			// 添加到client列表中
			clientPool.put(host + ":" + port, client);
			
			return client;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
