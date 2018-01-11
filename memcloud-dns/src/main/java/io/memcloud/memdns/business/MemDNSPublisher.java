package io.memcloud.memdns.business;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.memcloud.memdns.dao.IAppConfAuditDao;
import io.memcloud.memdns.dao.IAppConfDao;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppConf;
import io.memcloud.memdns.dao.entry.AppConfAudit;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;

public class MemDNSPublisher implements IMemDNSPublisher {

	protected static final Logger log = LoggerFactory.getLogger(MemDNSPublisher.class);
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;
	
	@Resource(name = "appConfDao")
	private IAppConfDao appConfDao;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "appConfAuditDao")
	private IAppConfAuditDao appConfAuditDao;

	
	/**
	 * 警告：本实现不可在高并发环境使用，没考虑 Lost Update 的问题。
	 * */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	public AppConfAudit publishAppConf(AppDesc appDesc, long userId) {
		long appId = appDesc.getId();
		if (appDesc.getStatus() == AppDesc.STATUS_INITIALIZE) {
			throw new IllegalStateException("please allocate mem sharding before publishing");
		}
		/*
		 * 依据事务传播行为，保证发布配置和登记审计日志是一个事务.
		 * */
		List<AppMemGroup> group = appMemGroupDao.getByAppId(appId);
		if (group==null || group.size()<=0) {
			throw new IllegalArgumentException("appId "+appId+" not found in AppMemGroup");
		}
		StringBuffer groupText = new StringBuffer();
		for (AppMemGroup shard : group) {
			String master = shard.getMasterIP()+":"+shard.getMasterPort();
			String slave = shard.getSlaveIP()+":"+shard.getSlavePort();
			groupText.append(master).append(",").append(slave);
			groupText.append(" ");
		}
		if (groupText.length() > 0) {
			groupText.delete(groupText.length()-1, groupText.length());
		}
		/* 警告：并发很小，不考虑Lost Update导致的风险 */
		AppConf appConf = appConfDao.get(appId);
		boolean isInitPub = false;
		if (appConf == null) {//首次发布
			appConf = new AppConf();
			appConf.setVersion(0);//后面会加1，首次发布版本号定义为1.
			isInitPub = true;
		} 
		/* 首次发布，需要填写其他字段； 不是首次发布，只有配置串变更了，才需要更新 */
		boolean isModified = isInitPub || !groupText.toString().equals(appConf.getGroupText()); 
		if (isModified) {
			appConf.setId(appId);
			appConf.setVersion(appConf.getVersion()+1);
			appConf.setShardNum(group.size());
			appConf.setGroupText(groupText.toString());
		}
		if (isInitPub) {//首次发布
			appConfDao.save(appConf);
		} else {//非首次发布，只有需要更新才更新（没变化的，支持幂等）
			if (isModified) {
				appConfDao.update(appConf);
			}
		}
		
		//修改AppDesc的状态
		if (appDesc.getStatus() != AppDesc.STATUS_PUBLISHED) {
			appDesc.setStatus(AppDesc.STATUS_PUBLISHED);
			appDescDao.update(appDesc);
		}
		
		
		//登记审计日志
		AppConfAudit audit = new AppConfAudit();
		audit.setAppId(appId);
		audit.setOperUid(userId);
		audit.setGroupText(appConf.getGroupText());
		audit.setShardNum(appConf.getShardNum());
		audit.setVersion(appConf.getVersion());
		if (isInitPub || isModified) {
			log.info("insert audit log for appid {} by uid {}",appId,userId);
			appConfAuditDao.save(audit);
		} else {
			log.info("return audit log for appid {} by uid {}",appId,userId);
		}
		return audit;
	}


	@Override
	public AppConf getAppConf(long appId) {
		return appConfDao.get(appId);
	}
	
}
