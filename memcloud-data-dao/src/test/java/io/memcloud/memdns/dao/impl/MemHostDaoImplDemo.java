package io.memcloud.memdns.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IMemHostDao;
import io.memcloud.memdns.dao.entry.MemHost;

public class MemHostDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IMemHostDao memHostDao = (IMemHostDao) applicationContext.getBean("memHostDao");
		
//		MemHost memHost = new MemHost();
//		memHost.setIp("10.10.83.83");
//		memHost.setSshUser("root");
//		memHost.setSshPwd("123456");
//		memHostDao.save(memHost);
//		System.out.println(memHost.getId());
		
		MemHost h1 = memHostDao.getByIP("10.10.83.83");
		System.out.println(h1.getIp());
	}

}
