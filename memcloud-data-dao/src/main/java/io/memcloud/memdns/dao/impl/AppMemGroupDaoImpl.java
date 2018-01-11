package io.memcloud.memdns.dao.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppMemGroup;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class AppMemGroupDaoImpl implements IAppMemGroupDao {

	@Resource(name = "entityDao")
	private IEntityDao<AppMemGroup, Long> entityDao;
	
	@Override
	public AppMemGroup getByRecId(Long id) {
		return entityDao.get(AppMemGroup.class, id);
	}

	@Override
	public List<AppMemGroup> getByAppId(Long appId) {
		final Object[] conditions = new Object[] { appId };
		final String hql = "select g from AppMemGroup g where g.appId = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 100);//假设一个AppID最多100个分片
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<AppMemGroup> r = new LinkedList<AppMemGroup>();
		for (Object obj : idList) {//量不大
			r.add((AppMemGroup)obj);
		}
		return r;
	}
	
	@Override
	public List<AppMemGroup> getByAppId(List<Long> appIds) {
		if (appIds==null || appIds.size() <=0) {
			throw new IllegalArgumentException("appIds is empty");
		}
		StringBuffer scope = new StringBuffer();
		for (Long aid : appIds) {
			scope.append(aid).append(",");
		}
		scope.delete(scope.length()-1, scope.length());
		
		final String hql = "select g from AppMemGroup g where g.appId IN ( "+scope.toString()+" )";

		List<Object> idList = entityDao.execHQLList(hql, new Object[] {}, 200);//假设最多200个
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<AppMemGroup> r = new LinkedList<AppMemGroup>();
		for (Object obj : idList) {//量不大
			r.add((AppMemGroup)obj);
		}
		return r;
	}
	
	@Override
	public LinkedHashMap<Long, Long> shardCount(List<Long> appIds) {
		if (appIds==null || appIds.size() <=0) {
			throw new IllegalArgumentException("appIds is empty");
		}
		LinkedHashMap<Long, Long> cv = new LinkedHashMap<Long, Long>(appIds.size());//counter vector
		StringBuffer scope = new StringBuffer();
		for (Long aid : appIds) {
			scope.append(aid).append(",");
		}
		scope.delete(scope.length()-1, scope.length());
		
		/*
		 * 注意：如果AppID不存在，SELECT结果则不包含该AppID，因此SELECT投影必须包含AppID列。
		 * 比如： select appid, count(appid) as count from app_mem_group where appid in (5,1,2) group by appid
		 * 返回：(appid,count) (2,3) (5,1) 不包含AppID=1的Count
		 * */
		final String hql = "select g.appId, count(g.appId) as count from AppMemGroup g where g.appId IN ( "+scope.toString()+" ) group by g.appId";

		List<Object> idList = entityDao.execHQLList(hql, new Object[] {}, 200);//假设最多200个
		if (idList == null || idList.size() == 0) {
			return null;
		}
		for (Object obj : idList) {//obj的类型是长度为2的数组Object[2]，其中：obj[0]是Long类型，obj[1]也是Long类型
			Object[] tuples = (Object[])obj;
			cv.put((Long)tuples[0], (Long)tuples[1]);
		}
		return cv;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	public Long save(AppMemGroup appMemGroup) {
		return entityDao.save(appMemGroup);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	public void save(List<AppMemGroup> appShards) {
		if (appShards != null && appShards.size() > 1000) {//本实现不适合一次性插入过多记录，容易导致内存溢出
			throw new IllegalArgumentException("too large list size for this implementation because of potential Out of Memory");
		}
		for (AppMemGroup s : appShards) {
			save(s);//批量插入整体作为一个事务，Hibernate的Session缓存积攒并一次性提交。注意此处不是在AOP对象上调用save()不会产生传播行为的叠加效应。
		}
	}

	@Override
	public void update(AppMemGroup appMemGroup) {
		entityDao.update(appMemGroup);
	}

	@Override
	public void delete(Long id) {
		entityDao.delete(AppMemGroup.class, id);
	}

	@Override
	public AppMemGroup getByHostAndPort(String host, Integer port) {
		final Object[] conditions = new Object[] { host, port, host ,port };
		final String hql = "select g from AppMemGroup g where (g.masterIP = ? and g.masterPort=?) or" +
				" (g.slaveIP = ? and g.slavePort=? ) ";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		return (AppMemGroup)idList.get(0);
	}

}
