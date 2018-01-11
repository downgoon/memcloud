package io.memcloud.stats.business.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.downgoon.jresty.commons.utils.concurrent.ConcurrentResourceContainer;
import com.github.downgoon.jresty.commons.utils.concurrent.ResourceLifecycle;
import io.memcloud.stats.business.IMemInstanceConnectionPool;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * @author ganghuawang
 * 初始化memcached客户端，并监听状态
 */
public class ConcurrentMemInstanceConnectionPool implements IMemInstanceConnectionPool {

	private static Logger LOG = LoggerFactory.getLogger(ConcurrentMemInstanceConnectionPool.class);
	
	private ResourceLifecycle<MemcachedClient> clientLifecycle = new MemcachedClientLifecycle();
	
	private ConcurrentResourceContainer<MemcachedClient> clientPool = new ConcurrentResourceContainer<MemcachedClient>(clientLifecycle);

	@Override
	public Map<String, MemcachedClient> getConnectionPool() {
		return clientPool.container();
	}
	
	@Override
	public MemcachedClient addClient(String host, int port) {
		String name = host + ":" + port;
		MemcachedClient client = null;
		try {
			client = clientPool.addResource(name);
		} catch (Exception e) {
			LOG.warn("xmemcached client build exception on {}", name, e);
			return null;
		}
		LOG.info("xmemcached client list size :" + clientPool.size());
		return client;
	}

	@Override
	public void removeClient(String host, int port) {
		String name = host + ":" + port;
		try {
			clientPool.removeResource(name);
		} catch (Exception e) { // IOException
			LOG.warn("remove xmemcached client exception", e);
			throw new IllegalStateException("MemcachedClient Close Exception", e);
		}
	}
	
	@Override
	public boolean hasClient(String host, int port) {
		String name = host + ":" + port;
		return clientPool.containsName(name);
	}


	private class MemcachedClientLifecycle implements ResourceLifecycle<MemcachedClient> {

		@Override
		public MemcachedClient buildResource(String name) throws Exception {
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					AddrUtil.getAddresses(name)); // host:port
			
			// client参数优化
			builder.setConnectionPoolSize(1);
			builder.getConfiguration().setSessionIdleTimeout(20000); // 设为如果连接超时没有任何IO操作就发起心跳检测，默认5秒
			builder.getConfiguration().setStatisticsServer(false);	// 关闭客户端统计
			
			// 创建client
			MemcachedClient client = builder.build();
			return client;
		}

		@Override
		public void destoryResource(String name, MemcachedClient client) throws Exception {
			client.shutdown();
		}
		
	}
	
}
