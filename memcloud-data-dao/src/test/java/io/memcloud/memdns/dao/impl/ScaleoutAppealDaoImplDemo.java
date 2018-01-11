package io.memcloud.memdns.dao.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IScaleoutAppealDao;
import io.memcloud.memdns.dao.entry.ScaleoutAppeal;

public class ScaleoutAppealDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IScaleoutAppealDao scaleoutAppealDao = (IScaleoutAppealDao) applicationContext.getBean("scaleoutAppealDao");
//		ScaleoutAppeal sa = new ScaleoutAppeal();
//		sa.setAppId(5L);
//		sa.setUserId(1001L);
//		sa.setStatus(ScaleoutAppeal.STATUS_WAITING);
//		sa.setShardNum(3);
//		sa.setMemSize(1024);
//		sa.setCreateTime(System.currentTimeMillis());
//		Long id = scaleoutAppealDao.save(sa);
//		System.out.println(id);
		
//		List<ScaleoutAppeal> list = scaleoutAppealDao.getByUid(1001L);
//		System.out.println(list);
		
		List<ScaleoutAppeal> waiting = scaleoutAppealDao.getStatusRecent(ScaleoutAppeal.STATUS_WAITING);
		System.out.println(waiting);
		
	}

}
