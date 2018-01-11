package io.memcloud.memdns.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IAppConfDao;
import io.memcloud.memdns.dao.entry.AppConf;

public class AppConfDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IAppConfDao appConfDao = (IAppConfDao) applicationContext.getBean("appConfDao");
		AppConf appConf = new AppConf();
		appConf.setGroupText("1122");
		appConf.setVersion(1);
		appConf.setShardNum(5);
		appConf.setId(10001L);
		appConfDao.save(appConf);
		
	}

}
