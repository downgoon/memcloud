package io.memcloud.memdns.dao.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IAppMemGroupDao;

public class AppMemGroupDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IAppMemGroupDao appMemGroupDao = (IAppMemGroupDao) applicationContext.getBean("appMemGroupDao");
		
		/*
10.10.79.75:14501    ,   10.11.132.56:14502
10.11.132.56:14503    ,   10.10.79.75:14504
10.10.79.75:14505    ,   10.11.132.56:14506
10.11.132.56:14507    ,   10.10.79.75:14508
		 * */
//		AppMemGroup s1 = new AppMemGroup();//分片1
//		s1.setAppId(2L);
//		s1.setMasterIP("10.10.79.75");
//		s1.setMasterPort(14501);
//		s1.setSlaveIP("10.11.132.56");
//		s1.setSlavePort(14502);
//		s1.setStatus(0);
//		appMemGroupDao.save(s1);
		
//		AppMemGroup s2 = new AppMemGroup();//分片2
//		s2.setAppId(2L);
//		s2.setMasterIP("10.11.132.56");
//		s2.setMasterPort(14503);
//		s2.setSlaveIP("10.10.79.75");
//		s2.setSlavePort(14504);
//		s2.setStatus(0);
////		appMemGroupDao.save(s2);
//		
//		AppMemGroup s3 = new AppMemGroup();//分片2
//		s3.setAppId(2L);
//		s3.setMasterIP("10.10.79.75");
//		s3.setMasterPort(14505);
//		s3.setSlaveIP("10.11.132.56");
//		s3.setSlavePort(14506);
//		s3.setStatus(0);
////		appMemGroupDao.save(s3);
//		
//		List<AppMemGroup> shards = Arrays.asList(new AppMemGroup[] {s2, s3});
//		appMemGroupDao.save(shards);
		
//		List<AppMemGroup> g = appMemGroupDao.getByAppId(Arrays.asList(new Long[] {2L,5L}));
//		System.out.println(g);

		 LinkedHashMap<Long,Long> cv = appMemGroupDao.shardCount(Arrays.asList(new Long[] {2L,5L,1L}));
		 System.out.println(cv);
		
		
//		AppDesc appTop = new AppDesc();
//		appTop.setName("top");
//		appTop.setDescr("排行榜");
//		appTop.setStatus(0);
//		appTop.setOwnerUid(10001L);
//		appTop.setCreateTime(System.currentTimeMillis());
//		appTop.setPassedTime(null);
//		
//		appDescDao.save(appTop);
//		
//		AppDesc appSpace = new AppDesc();
//		appSpace.setName("space");
//		appSpace.setDescr("空间集群");
//		appSpace.setStatus(0);
//		appSpace.setOwnerUid(10001L);
//		appSpace.setCreateTime(System.currentTimeMillis());
//		appSpace.setPassedTime(null);
//		
//		appDescDao.save(appSpace);
		
//		AppDesc app = appDescDao.getByAppName("index");
//		System.out.println(app);
//		
	}

}
