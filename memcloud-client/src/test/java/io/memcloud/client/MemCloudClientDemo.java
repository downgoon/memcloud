package io.memcloud.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.memcloud.client.MemCloudClient;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

public class MemCloudClientDemo {

	public static void main(String[] args) throws Exception {
		XMemcachedClient memClient = genMemcachedClient();
		MemDNSRefreshMock memDNSMock = new MemDNSRefreshMock();
		
		MemCloudClient mcc = MemCloudClient.build("appid", memDNSMock, memClient);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("press to start >"); 
		reader.readLine();
		memDNSMock.mock(new int[] {0}, 1);
		mcc.start();

		System.out.print("press to append >"); 
		reader.readLine();
		memDNSMock.mock(new int[] {0,1}, 2);
		
		System.out.print("press to remove >"); 
		reader.readLine();
		memDNSMock.mock(new int[] {1}, 3);
		
		System.out.print("press to stop >"); 
		reader.readLine();
		mcc.stop();
		
	}
	
	
	
	private static XMemcachedClient genMemcachedClient() throws Exception {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder();
		builder.setFailureMode(true);
		MemcachedClient mclient = builder.build();
		return (XMemcachedClient)mclient;
	}

}
