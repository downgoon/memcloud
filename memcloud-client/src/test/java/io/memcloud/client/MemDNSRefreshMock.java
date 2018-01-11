package io.memcloud.client;

import java.util.ArrayList;
import java.util.List;

import io.memcloud.client.IMemDNSRefresh;
import io.memcloud.client.MemDNSLookup;
import io.memcloud.client.MemShard;

public class MemDNSRefreshMock implements IMemDNSRefresh {

	private MemShard S0 = new MemShard("10.11.132.12:10081","10.11.132.12:10082");
	private MemShard S1 = new MemShard("10.11.132.12:10091","10.11.132.12:10092");
	
	private MemShard[] SA = new MemShard[] {S0, S1};
	
	private int version = 1;
	private int ttl = 3;
	private int[] shards = new int[] {0};
	
	public void mock(int[] shards) {
		this.shards = shards;
	}
	
	public void mock(int[] shards, int version) {
		this.shards = shards;
		this.version = version;
	}
	
	public void mock(int[] shards, int version, int ttl) {
		this.shards = shards;
		this.version = version;
		this.ttl = ttl;
	}
	
	private boolean flagException; 
	public void mockExceptionOneTime(boolean flagException) {
		this.flagException = flagException;
	}
	
	private boolean flagExceptionAlways;
	public void mockExceptionAlways(boolean flag) {
		this.flagExceptionAlways = flag;
	}
	
	private boolean flagNULL;
	public void mockNULLOneTime(boolean flagNULL) {
		this.flagNULL = flagNULL;
	}
	
	private boolean flagNULLAlways;
	public void mockNULLAlways(boolean flag) {
		this.flagNULLAlways = flag;
	}
	
	
	@Override
	public MemDNSLookup refreshDNS(String appid) {
		if (flagException) {
			flagException = false;
			throw new RuntimeException("mock exception");
		}
		if (flagNULL) {
			flagNULL = false;
			return null;
		}
		
		if (flagExceptionAlways) {
			throw new RuntimeException("mock exception");
		}
		if (flagNULLAlways) {
			return null;
		}
		
		MemDNSLookup dns = new MemDNSLookup();
		dns.setVersion(version);
		dns.setTTLSecond(ttl);
		List<MemShard> group = new ArrayList<MemShard>();
		for (int sidx : shards) {
			group.add(SA[sidx]);
		}
		dns.setMemGroup(group);
		dns.setTimestamp(System.currentTimeMillis());
		return dns;
	}

}
