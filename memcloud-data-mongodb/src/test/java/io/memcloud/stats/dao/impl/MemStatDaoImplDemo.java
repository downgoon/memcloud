package io.memcloud.stats.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.driver.mongodb.IMongodbStatManager;
import io.memcloud.driver.mongodb.impl.MongodbStatManager;
import io.memcloud.stats.dao.IMemStatDao;
import io.memcloud.stats.model.MemStatSummary;

public class MemStatDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IMongodbStatManager mongodbStatManager = (MongodbStatManager)factory.getBean("mongodbStatManager");
		IMemStatDao memStatDao = new MemStatDaoImpl(mongodbStatManager);
		
//		LinkedHashMap<String, Long> f = memStatDao.trendGet("10.10.79.214", 11211, DateUtil.todayBegin());
//		System.out.println(f);
		
		MemStatSummary  s = memStatDao.summary("10.10.79.214", 11211);
		System.out.println(s);
		
	}

}
