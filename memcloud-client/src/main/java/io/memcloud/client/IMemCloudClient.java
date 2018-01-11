package io.memcloud.client;

import net.rubyeye.xmemcached.XMemcachedClient;

public interface IMemCloudClient {

	public abstract void start();

	public abstract void stop();

	public abstract boolean isEnableDNSRefresh();
	
	public String getAppid();

	public abstract XMemcachedClient getMemcachedClient();

}
