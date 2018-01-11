package io.memcloud.memdns.dao.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IMemInstanceDao;
import io.memcloud.memdns.dao.entry.MemInstancePeer;

public class MemInstanceDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IMemInstanceDao memInstanceDao = (IMemInstanceDao) applicationContext.getBean("memInstanceDao");
		
//		MemInstance m = new MemInstance();
//		m.setHostid(12L);
//		m.setHostip("10.10.83.83");
//		m.setPort(11211);
//		m.setArgMem(1024);
//		m.setArgConn(256);
//		m.setArgConf("");
//		
//		long id = memInstanceDao.save(m);
//		System.out.println(id);
//	
//		MemInstance m = memInstanceDao.get("10.10.83.83", 11211);
//		System.out.println(m);
		
//		List<MemInstancePeer> peers = memInstanceDao.getUnusedPeer(0,10,1000);
//		System.out.println("资源池结果："+peers);
//		int count = memInstanceDao.getUnusedPeerCount(1000);
//		System.out.println("资源数量："+count);
		
		
//		List<Long> ids = Arrays.asList(new Long[] {3L,4L});
//		memInstanceDao.allocUnusedPeer(ids);
		List<MemInstancePeer> ps = memInstanceDao.getUnusedPeer(0, 20, 0);
		System.out.println(ps);
		
		
	}

}
