package io.memcloud.memdns.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.entry.AppDesc;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class AppDescDaoImpl implements IAppDescDao {

	@Resource(name = "entityDao")
	private IEntityDao<AppDesc, Long> entityDao;
	
	@Override
	public List<AppDesc> getByUid(Long uid) {
		final Object[] conditions = new Object[] { uid };
		final String hql = "select app from AppDesc app where app.ownerUid = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 100);//假设一个人最多注册100个应用
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<AppDesc> r = new LinkedList<AppDesc>();
		for (Object obj : idList) {
			r.add((AppDesc)obj);
		}
		return r;
	}

	@Override
	public AppDesc getByAppName(String appName) {
		final Object[] conditions = new Object[] { appName };
		final String hql = "select app from AppDesc app where app.name = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);//应用名称是唯一的
		if (idList == null || idList.size() == 0) {
			return null;
		}
		return (AppDesc)idList.get(0);
	}



	@Override
	public AppDesc getByAppId(Long appId) {
		return entityDao.get(AppDesc.class, appId);
	}

	@Override
	public Long save(AppDesc appDesc) {
		return entityDao.save(appDesc);
	}

	@Override
	public void update(AppDesc appDesc) {
		entityDao.update(appDesc);
	}

	@Override
	public void delete(Long id) {
		entityDao.delete(AppDesc.class, id);
	}

}
