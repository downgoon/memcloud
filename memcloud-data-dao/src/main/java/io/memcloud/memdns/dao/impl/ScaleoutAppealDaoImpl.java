package io.memcloud.memdns.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IScaleoutAppealDao;
import io.memcloud.memdns.dao.entry.ScaleoutAppeal;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class ScaleoutAppealDaoImpl implements IScaleoutAppealDao {

	@Resource(name = "entityDao")
	private IEntityDao<ScaleoutAppeal, Long> entityDao;
	
	@Override
	public List<ScaleoutAppeal> getByUid(Long uid) {
		final Object[] conditions = new Object[] { uid };
		final String hql = "select sa from ScaleoutAppeal sa where sa.userId = ? order by sa.id desc";//按时间顺序倒排 （等效：order by sa.createTime desc）

		List<Object> idList = entityDao.execHQLList(hql, conditions, 100);//假设一个人最多申请100次
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<ScaleoutAppeal> r = new LinkedList<ScaleoutAppeal>();
		for (Object obj : idList) {
			r.add((ScaleoutAppeal)obj);
		}
		return r;
	}
	
	@Override
	public List<ScaleoutAppeal> getByUid(Long userId, Long beginTime,
			Long endTime) {
		final Object[] conditions = new Object[] { userId, beginTime, endTime };
		final String hql = "select sa from ScaleoutAppeal sa where sa.userId = ? and sa.createTime >= ? and sa.createTime <= ? order by sa.id desc";
		List<Object> idList = entityDao.execHQLList(hql, conditions, 100);//假设一个人最多申请100次
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<ScaleoutAppeal> r = new LinkedList<ScaleoutAppeal>();
		for (Object obj : idList) {
			r.add((ScaleoutAppeal)obj);
		}
		return r;
	}


	private static final long DAY15 = 1000L*60*60*24*15;
	
	/** 最近15天 */
	@Override
	public List<ScaleoutAppeal> getStatusRecent(short status) {
		long tmNow = System.currentTimeMillis();
		long tmAgo15Day = tmNow - DAY15;
		return getStatusList(status, tmAgo15Day, tmNow);
	}
	
	

	@Override
	public List<ScaleoutAppeal> getStatusList(short status, Long beginTime, Long endTime) {
		final Object[] conditions = new Object[] { status, beginTime, endTime };
		final String hql = "select sa from ScaleoutAppeal sa where sa.status = ? and sa.createTime >= ? and sa.createTime <= ? ";
		List<Object> idList = entityDao.execHQLList(hql, conditions, 100);//假设一个人最多申请100次
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<ScaleoutAppeal> r = new LinkedList<ScaleoutAppeal>();
		for (Object obj : idList) {
			r.add((ScaleoutAppeal)obj);
		}
		return r;
	}

	@Override
	public ScaleoutAppeal get(Long id) {
		return entityDao.get(ScaleoutAppeal.class, id);
	}
	
	@Override
	public void delete(Long id) {
		entityDao.delete(ScaleoutAppeal.class,id);
	}
	
	@Override
	public Long save(ScaleoutAppeal scaleAppeal) {
		return entityDao.save(scaleAppeal);
	}

	@Override
	public void update(ScaleoutAppeal scaleAppeal) {
		entityDao.update(scaleAppeal);
	}

}
