package io.memcloud.memdns.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.memcloud.memdns.dao.IMemInstanceDao;
import io.memcloud.memdns.dao.entry.MemInstance;
import io.memcloud.memdns.dao.entry.MemInstancePeer;

import com.github.downgoon.jresty.data.dao.IEntityDao;

public class MemInstanceDaoImpl implements IMemInstanceDao {

	@Resource(name = "entityDao")
	private IEntityDao<MemInstance, Long> entityDao;
	
	@Override
	public MemInstance get(String ip, Integer port) {
		final Object[] conditions = new Object[] { ip, port };
		final String hql = "select m from MemInstance m where m.hostIp = ? and m.port = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 2);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		if (idList.size() > 1) {
			throw new IllegalStateException("unique index broken in MemInstance ["+ip+":"+port+"]");
		}
		return (MemInstance)idList.get(0);
	}
	
	@Override
	public MemInstance getByRepc(String ip, Integer repcPort) {
		final Object[] conditions = new Object[] { ip, repcPort };
		final String hql = "select m from MemInstance m where m.hostIp = ? and m.repcPort = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 2);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		if (idList.size() > 1) {
			throw new IllegalStateException("unique index broken in MemInstance ["+ip+":"+repcPort+"]");
		}
		return (MemInstance)idList.get(0);
	}

	@Override
	public MemInstance get(Long id) {
		return entityDao.get(MemInstance.class, id);
	}

	@Override
	public Long save(MemInstance memInstance) {
		return entityDao.save(memInstance);
	}

	@Override
	public void update(MemInstance memInstance) {
		entityDao.update(memInstance);
	}

	@Override
	public void delete(Long id) {
		entityDao.delete(MemInstance.class, id);
	}

	@Override
	public List<MemInstance> get() {
		List<MemInstance> list = new ArrayList<MemInstance>();
		final Object[] conditions = new Object[] { };
		final String hql = "select m from MemInstance m ";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return list;
		}else{
			for(Object obj : idList){
				list.add((MemInstance)obj);
			}
		}
		return list;
	}

	
	
	@Override
	public int getUnusedPeerCount(int memSizeM) {
		final Object[] conditions = new Object[] { MemInstance.STATUS_UNUSE, MemInstance.ELDER_IN_PEER, memSizeM};
		final String hql = "select count(m1.id)" +
				" from MemInstance m1, MemInstance m2 " +
				" where m1.hostIp=m2.peerIp and m1.repcPort=m2.repcPort and m1.status=? and m1.roleInPeer=? and m1.argMem >= ?";
		
		List<Object> idList = entityDao.execHQLList(hql, conditions, 2);
		if (idList == null || idList.size() == 0) {
			return 0;
		}
		return ((Long)idList.get(0)).intValue();
	}


	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	@Override
	public List<MemInstancePeer> getUnusedPeer(int offset, int pageSize,int memSizeM) {
		if ( memSizeM < 100 ) {
			throw new IllegalArgumentException("memSizeM too small to be allocated : "+memSizeM);
		}
		final Object[] conditions = new Object[] { MemInstance.STATUS_UNUSE, MemInstance.ELDER_IN_PEER, memSizeM};
		final String hql = "select new io.memcloud.memdns.dao.entry.MemInstancePeer( " +
				" m1.repcPort as repcPort,m1.hostIp as mhost, m1.port as mport, m1.id as mid, m2.hostIp as shost, m2.port as sport, m2.id as sid, m1.argMem as mem )" +
				" from MemInstance m1, MemInstance m2 " +
				" where m1.hostIp=m2.peerIp and m1.repcPort=m2.repcPort and m1.status=? and m1.roleInPeer=? and m1.argMem >= ?" +
				" order by m1.argMem, m1.id";
		/*
		 * Exception in thread "main" org.springframework.orm.hibernate3.HibernateQueryException: Unable to locate appropriate constructor on class
		 * ORMapping for MemInstancePeer depends on MemInstancePeer's constructor 
		 * */

		List<Object> idList = entityDao.execHQLList(hql, conditions, offset, pageSize);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		List<MemInstancePeer> peers = new ArrayList<MemInstancePeer>(idList.size());
		for(Object obj : idList) {
			peers.add((MemInstancePeer)obj);
		}
		return peers;
	}

	
	/**
	 * 注意：实现上采用了原生SQL，以达到采用乐观锁发现高并发导致的Lost Update 问题。
	 * */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	@Override
	public int allocUnusedPeer(List<Long> instanceIDs) {
		if (instanceIDs==null || instanceIDs.size()<=0) {
			throw new IllegalArgumentException("instance ids should be not empty");
		}
		StringBuffer ids = new StringBuffer();
		for (Long id : instanceIDs) {
			ids.append(id).append(",");
		}
		ids.delete(ids.length()-1, ids.length());
		
//		final Object[] cond = new Object[] { MemInstance.STATUS_USED, ids, MemInstance.STATUS_UNUSE};
//		final String hql = "update mem_instance as m set m.status = ? where m.id in ( ? ) and m.status = ? ";
//		int c = entityDao.batchUpdate(hql, Arrays.asList(cond));//原生SQL
//		System.out.println(c);
		
		/* 采用Version Compare 乐观锁机制 发现由高并发导致的 Lost Update 问题 */
		final String hql = "update mem_instance as m set m.status = "+MemInstance.STATUS_USED+" where m.id in ( "+ids+" ) and m.status = "+MemInstance.STATUS_UNUSE;
		int c = entityDao.batchUpdate(hql, null);//原生SQL
		return c;
	}
	
	@Override
	public List<MemInstance> getAll() {
		List<MemInstance> list = new ArrayList<MemInstance>();
		final Object[] conditions = new Object[] {MemInstance.STATUS_USED};
		final String hql = "select m from MemInstance m where m.status = ?";
		
		List<Object> idList = entityDao.execHQLList(hql, conditions, 1000);
		if (idList == null || idList.size() == 0) {
			return list;
		}else{
			for(Object obj : idList){
				list.add((MemInstance)obj);
			}
		}
		return list;
	}
	
}


