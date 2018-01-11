package io.memcloud.client;

import java.util.ArrayList;
import java.util.List;

import io.memcloud.client.MemShard;

public class MemShardDemo {

	public static void main(String[] args) {
		demoGroup2conf();
	}
	
	public static void demoGroup2conf() {
		MemShard s1 = new MemShard();
		s1.setMaster("10.10.83.84:10081");
		s1.setSlave("10.10.83.83:10082");
		
		MemShard s2 = new MemShard();
		s2.setMaster(s1.getMaster());
		s2.setSlave(s1.getSlave());
		
		List<MemShard> group = new ArrayList<MemShard>();
		group.add(s1);
		group.add(s2);
		
		String conf = MemShard.group2conf(group);
		System.out.println("|"+conf+"|");
	}
	
	public static void demo() {
		MemShard s1 = new MemShard();
		s1.setMaster("10.10.83.84:10081");
		s1.setSlave("10.10.83.83:10082");
		
		MemShard s2 = new MemShard();
		s2.setMaster(s1.getMaster());
		s2.setSlave(s1.getSlave());
		
		System.out.println(s1.equals(s2));
		System.out.println(s1.equals(s1));
		
		MemShard s3 = new MemShard();
		s3.setMaster(s1.getMaster());
		System.out.println(s1.equals(s3));
	}

}
