package io.memcloud.client;

import io.memcloud.client.MemDNSLookup;
import io.memcloud.client.MemDNSRefresh;

public class MemDNSRefreshDemo {

	public static void main(String[] args) {
		MemDNSRefresh memDNS = new MemDNSRefresh();
		MemDNSLookup lookup = memDNS.refreshDNS("10001");
		System.out.println(lookup);
		System.out.println(lookup.getVersion());
		System.out.println(lookup.getMemGroup());
	}

}
