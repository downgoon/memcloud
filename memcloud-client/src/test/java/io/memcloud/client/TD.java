package io.memcloud.client;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.memcloud.client.IMemDNSRefresh;
import io.memcloud.client.MemCloudClient;
import io.memcloud.client.MemDNSRefresh;


public class TD {

	private static final Logger log = LoggerFactory.getLogger("MemCloud");
	
	public static void main(String[] args) throws Exception {
		log.info("MemCloudClientDemo main start ");
//		MemCloudClient mcc = MemCloudClient.build("10001", genMemDNSRefresh(), genMemcachedClient());
		MemCloudClient mcc = MemCloudClient.buildDefault("10001", null);
		
		try {
			//存储
			int i = 0;
			while ( i < 10) {
				mcc.getMemcachedClient().set("k"+i, 60*5, "v"+i);
				i++;
			}
			
			//提取
			i = 0;
			while (i<10) {
				Object v = mcc.getMemcachedClient().get("k"+i);
				System.out.println(v);
				i++;
			}
			
		} catch (MemcachedException me) {
			log.error("异常输出到memcloud.log",me);
		}
		
		
	}
	
	static IMemDNSRefresh genMemDNSRefresh() {
		return new MemDNSRefresh();
	}
	
	static XMemcachedClient genMemcachedClient() throws IOException   {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder();
		builder.setFailureMode(true);
		MemcachedClient mclient = builder.build();
		return (XMemcachedClient)mclient;
	}

}
