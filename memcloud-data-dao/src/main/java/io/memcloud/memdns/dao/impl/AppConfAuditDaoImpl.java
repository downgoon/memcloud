package io.memcloud.memdns.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IAppConfAuditDao;
import io.memcloud.memdns.dao.entry.AppConfAudit;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class AppConfAuditDaoImpl implements IAppConfAuditDao {
	
	@Resource(name = "entityDao")
	private IEntityDao<AppConfAudit, Long> entityDao;

	@Override
	public AppConfAudit getByRecId(Long id) {
		return entityDao.get(AppConfAudit.class, id);
	}

	@Override
	public List<AppConfAudit> getByAppId(Long appid) {
		final Object[] conditions = new Object[] { appid };
		final String hql = "select audit from AppConfAudit audit where audit.appId = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<AppConfAudit> r = new LinkedList<AppConfAudit>();
		for (Object obj : idList) {//量不大
			r.add((AppConfAudit)obj);
		}
		return r;
	}

	@Override
	public Long save(AppConfAudit appConfAudit) {
		return entityDao.save(appConfAudit);
	}

	@Override
	public void update(AppConfAudit appConfAudit) {
		entityDao.update(appConfAudit);
	}

	@Override
	public void delete(Long id) {
		entityDao.delete(AppConfAudit.class, id);
	}

}
