/**
 * 
 */
package io.memcloud.stats.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.entry.MemFault;
import io.memcloud.stats.dao.IMemFaultDao;

import com.github.downgoon.jresty.data.dao.IEntityDao;

/**
 * @author ganghuawang
 *
 */
public class MemFaultDaoImpl implements IMemFaultDao {

	@Resource(name = "entityDao")
	private IEntityDao<MemFault, Long> entityDao;
	
	/* (non-Javadoc)
	 * @see io.memcloud.stats.dao.IMemFaultDao#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		entityDao.delete(MemFault.class, id);
	}

	@Override
	public MemFault get(Long id) {
		return entityDao.get(MemFault.class, id);
	}

	@Override
	public Long save(MemFault memFault) {
		return entityDao.save(memFault);
	}
	

	@Override
	public void update(MemFault memFault) {
		entityDao.update(memFault);
	}

	@Override
	public List<MemFault> getByAppId(Long appId) {
		List<MemFault> list = new ArrayList<MemFault>();
		final Object[] conditions = new Object[] { appId };
		final String hql = "select m from MemFault m where m.appId = ? ";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return null;
		}else {
			for(Object obj : idList){
				list.add((MemFault)obj);
			}
		}
		return list;
	}

	@Override
	public MemFault getByHostAndPort(String ip, Integer port) {
		final Object[] conditions = new Object[] { ip, port };
		final String hql = "select m from MemFault m where m.ip=? and m.port=? order by m.createTime desc ";
		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		return (MemFault)idList.get(0);	
	}

	@Override
	public List<MemFault> getByGtTime(Long timeMillis) {
		List<MemFault> list = new ArrayList<MemFault>();
		final Object[] conditions = new Object[] { timeMillis };
		final String hql = "select m from MemFault m where m.createTime > ? ";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1000);
		if (idList == null || idList.size() == 0) {
			return list;
		}else {
			for(Object obj : idList){
				list.add((MemFault)obj);
			}
		}
		return list;
	}

}
