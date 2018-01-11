package io.memcloud.memdns.dao.impl;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IAppConfDao;
import io.memcloud.memdns.dao.entry.AppConf;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class AppConfDaoImpl implements IAppConfDao {

	@Resource(name = "entityDao")
	private IEntityDao<AppConf, Long> entityDao;
	
	@Override
	public AppConf get(Long appId) {
		return entityDao.get(AppConf.class, appId);
	}

	@Override
	public Long save(AppConf appConf) {
		return entityDao.save(appConf);
	}

	@Override
	public void update(AppConf appConf) {
		entityDao.update(appConf);
	}

	@Override
	public void delete(Long appId) {
		entityDao.delete(AppConf.class, appId);
	}

}
