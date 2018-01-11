package io.memcloud.memdns.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IMemHostDao;
import io.memcloud.memdns.dao.entry.MemHost;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class MemHostDaoImpl implements IMemHostDao {

	@Resource(name = "entityDao")
	private IEntityDao<MemHost, Long> entityDao;

	@Override
	public MemHost getByIP(String ip) {
//		final Object[] conditions = new Object[] { ip };
//		final String hql = "select h.id from MemHost h where h.ip = ?";
//		List<Long> idList = entityDao.getHQLList(hql, conditions, 1);
//		if (idList == null || idList.size() == 0) {
//			return null;
//		}
//		Long id = idList.get(0);
//		return get(id);
		
		final Object[] conditions = new Object[] { ip };
		final String hql = "select h from MemHost h where h.ip = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		return (MemHost)idList.get(0);
	}

	@Override
	public Long save(MemHost memHost) {
		return entityDao.save(memHost);
	}

	@Override
	public void update(MemHost memHost) {
		entityDao.update(memHost);
	}

	@Override
	public MemHost get(Long id) {
		return entityDao.get(MemHost.class, id);
	}
	
	@Override
	public void delete(Long id) {
		entityDao.delete(MemHost.class, id);
	}
	
}
