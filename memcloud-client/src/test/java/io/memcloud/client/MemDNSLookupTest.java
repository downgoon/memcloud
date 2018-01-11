package io.memcloud.client;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.memcloud.client.MemDNSLookup;
import io.memcloud.client.MemShard;

public class MemDNSLookupTest {
	
	private MemDNSLookup dnsV1_Low;//S1,S2
	private MemDNSLookup dnsV2_Ful;//S1,S2,S3
	private MemDNSLookup dnsV3_Hig;//S2,S3
	
	private MemShard S1 = new MemShard("10.10.83.84:10081","10.10.83.83:10082");
	private MemShard S2 = new MemShard("10.10.83.84:10083","10.10.83.83:10084");
	private MemShard S3 = new MemShard("10.10.83.84:10085","10.10.83.83:10086");

	@Before
	public void setUp() throws Exception {
		dnsV1_Low = new MemDNSLookup();
		dnsV1_Low.setVersion(1);
		dnsV1_Low.setTTLSecond(10);
		List<MemShard> gLow = new ArrayList<MemShard>();
		gLow.add(S1);
		gLow.add(S2);
		dnsV1_Low.setMemGroup(gLow);

		dnsV2_Ful = new MemDNSLookup();
		dnsV2_Ful.setVersion(2);
		dnsV2_Ful.setTTLSecond(10);
		List<MemShard> gFul = new ArrayList<MemShard>();
		gFul.add(S1);
		gFul.add(S2);
		gFul.add(S3);
		dnsV2_Ful.setMemGroup(gFul);
		
		dnsV3_Hig = new MemDNSLookup();
		dnsV3_Hig.setVersion(3);
		dnsV3_Hig.setTTLSecond(10);
		List<MemShard> gHig = new ArrayList<MemShard>();
		gHig.add(S2);
		gHig.add(S3);
		dnsV3_Hig.setMemGroup(gHig);
		
	}

	@Test
	/** 第一次MemDNS查询 */
	public void testInit() {
		MemDNSLookup pre = null;
		List<MemShard> appended = dnsV1_Low.diffAppended(pre);
		System.out.println("init appended: "+appended);
		assertNotNull(appended);
		assertEquals(2, appended.size());
		assertEquals(S1, appended.get(0));
		assertEquals(S2, appended.get(1));
		
		List<MemShard> removed = dnsV1_Low.diffRemoved(pre);
		System.out.println("init removed: "+removed);
		assertTrue(removed==null || removed.size()==0);
	}
	
	@Test
	/** 扩容 ：V1->V2*/
	public void testScaleOut() {
		List<MemShard> appended = dnsV2_Ful.diffAppended(dnsV1_Low);
		System.out.println("scale out appended: "+appended);
		assertNotNull(appended);
		assertEquals(1, appended.size());
		assertEquals(S3, appended.get(0));
		
		List<MemShard> removed = dnsV2_Ful.diffRemoved(dnsV1_Low);
		System.out.println("scale out removed: "+removed);
		assertTrue(removed==null || removed.size()==0);
	}
	
	@Test
	/** 减少容量：V2->V3*/
	public void testHA() {
		List<MemShard> appended = dnsV3_Hig.diffAppended(dnsV2_Ful);
		System.out.println("HA appended: "+appended);
		assertTrue(appended==null || appended.size()==0);
		
		List<MemShard> removed = dnsV3_Hig.diffRemoved(dnsV2_Ful);
		System.out.println("HA removed: "+removed);
		
		assertNotNull(removed);
		assertEquals(1, removed.size());
		assertEquals(S1, removed.get(0));
	}
	
	@Test
	/** 综合：V1->V3*/
	public void testComp() {
		List<MemShard> appended = dnsV3_Hig.diffAppended(dnsV1_Low);
		System.out.println("Comp appended: "+appended);
		assertNotNull(appended);
		assertEquals(1, appended.size());
		assertEquals(S3, appended.get(0));
		
		List<MemShard> removed = dnsV3_Hig.diffRemoved(dnsV1_Low);
		System.out.println("Comp removed: "+removed);
		
		assertNotNull(removed);
		assertEquals(1, removed.size());
		assertEquals(S1, removed.get(0));
	}
	
	/** 相同版本号，不变更*/
	@Test
	public void testSameVersion() {
		Integer spot = dnsV3_Hig.getVersion();
		dnsV3_Hig.setVersion(dnsV1_Low.getVersion());
		
		List<MemShard> appended = dnsV3_Hig.diffAppended(dnsV1_Low);
		System.out.println("SameVersion appended: "+appended);
		assertTrue(appended==null || appended.size()==0);
		
		List<MemShard> removed = dnsV3_Hig.diffRemoved(dnsV1_Low);
		System.out.println("SameVersion removed: "+removed);
		assertTrue(removed==null || removed.size()==0);
		
		dnsV3_Hig.setVersion(spot);
	}
}
