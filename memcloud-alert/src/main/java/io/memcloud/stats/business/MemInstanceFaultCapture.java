package io.memcloud.stats.business;

import java.net.InetSocketAddress;

import javax.annotation.Resource;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientStateListener;

/**
 *  memcached客户端状态监听实现类
 */
public class MemInstanceFaultCapture implements MemcachedClientStateListener {

	@Resource( name = "memInstanceFaultManager")
	private IMemInstanceFaultManager memInstanceFaultManager;
	
	
	@Override
	public void onConnected(MemcachedClient arg0, InetSocketAddress arg1) {
		// 解除故障
		memInstanceFaultManager.recoverFault(arg1.getHostName(), arg1.getPort());
	}

	@Override
	public void onDisconnected(MemcachedClient arg0, InetSocketAddress arg1) {
		
	}

	@Override
	public void onException(MemcachedClient arg0, Throwable arg1) {
		// 解析host和port
		String failMess = arg1.getLocalizedMessage(); //Connect to 10.11.132.83:11213 fail,Connection refused: no further information
		String host = failMess.split(":")[0].split(" ")[2];
		String port = failMess.split(":")[1].split(" ")[0];
		// 故障报警
		memInstanceFaultManager.sendFaultMessage(host, Integer.valueOf(port));
	}

	@Override
	public void onShutDown(MemcachedClient arg0) {
		
	}

	@Override
	public void onStarted(MemcachedClient arg0) {
		
	}

	public IMemInstanceFaultManager getMemInstanceFaultManager() {
		return memInstanceFaultManager;
	}

	public void setMemInstanceFaultManager(IMemInstanceFaultManager memInstanceFaultManager) {
		this.memInstanceFaultManager = memInstanceFaultManager;
	}
	
	
	
}
