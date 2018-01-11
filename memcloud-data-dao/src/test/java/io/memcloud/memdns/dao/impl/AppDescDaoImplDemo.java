package io.memcloud.memdns.dao.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.entry.AppDesc;

public class AppDescDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IAppDescDao appDescDao = (IAppDescDao) applicationContext.getBean("appDescDao");
		
//		AppDesc appIndex = new AppDesc();
//		appIndex.setName("index");
//		appIndex.setDescr("指数在线业务系统集群");
//		appIndex.setStatus(0);
//		appIndex.setOwnerUid(10001L);
//		appIndex.setCreateTime(System.currentTimeMillis());
//		appIndex.setPassedTime(null);
//		
//		appDescDao.save(appIndex);
		
		
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
		List<AppDesc> list = appDescDao.getByUid(10001L);
		System.out.println(list);
	}

}
