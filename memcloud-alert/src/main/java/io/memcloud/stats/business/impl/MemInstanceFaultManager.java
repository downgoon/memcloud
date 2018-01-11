/**
 * 
 */
package io.memcloud.stats.business.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.memdns.dao.entry.MemFault;
import io.memcloud.stats.business.IMemInstanceFaultManager;
import io.memcloud.stats.dao.IMemFaultDao;
import io.memcloud.stats.notification.FaultEvent;
import io.memcloud.stats.notification.IFaultNotify;

/**
 * @author ganghuawang
 *
 */
public class MemInstanceFaultManager implements IMemInstanceFaultManager {

	private static final Logger log = Logger.getLogger(MemInstanceFaultManager.class);

	@Resource(name = "faultNotify")
	private IFaultNotify faultNotify;

	/** 故障连续超过3次，发送报警 */
	private int faultTimes = 3;

	/** 故障过期时间 （默认10分钟） */
	private static int expireSeconds = 600;

	private static boolean ischeck = false;

	/** 用于存放故障的次数和最近的一次报警时间 */
	private static Map<String, Long> faultResult = new ConcurrentHashMap<String, Long>();

	@Resource(name = "memFaultDao")
	private IMemFaultDao memFaultDao;

	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;

	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;

	public MemInstanceFaultManager() {

	}

	/**
	 * 确认故障已受理 1、更改故障的状态 2、发送消息提醒
	 */
	@Override
	public void receiveFaultMessage(Long id) {
		MemFault memFault = memFaultDao.get(id);
		if (memFault != null && memFault.getStatus() == 0) {
			memFault.setStatus(1);
			// 更改故障的状态
			memFaultDao.update(memFault);
			// 短信、邮件通知
			sendRecoveryMessage(memFault);
		}

		// 清空记录故障的次数
		String key = getKey(memFault.getIp(), memFault.getPort());
		faultResult.remove(key);
	}

	/**
	 * 故障自动恢复了
	 */
	@Override
	public void recoverFault(String host, Integer port) {
		MemFault memFault = memFaultDao.getByHostAndPort(host, port);
		if (memFault != null && memFault.getStatus() == 0) {
			receiveFaultMessage(memFault.getId());
		}
	}

	/**
	 * 1、确定是否出现故障，检验3次 2、如果是，记录故障信息 3、发出故障通知短信
	 */
	@Override
	public void sendFaultMessage(String host, Integer port) {
		initHistoryFaultTime();
		if (StringUtils.isEmpty(host) || port == null) {
			log.error("ip or port is null: ip=" + host + " port=" + port);
			return;
		}
		String key = getKey(host, port);

		// 检验超过次数才确认为故障
		Long times = faultResult.get(key) == null ? 0 : faultResult.get(key);
		times++;
		log.info("fault times :" + times + " host=" + host + " port=" + port);
		if (times < faultTimes) {
			// 故障次数累加
			faultResult.put(key, times);
		} else {
			// 清空故障的次数
			faultResult.remove(key);

			// 报警的时长间隔
			if (faultResult.get(key + "_time") == null) {
				faultResult.put(key + "_time", System.currentTimeMillis());

			} else {
				Long time = faultResult.get(key + "_time");
				if ((System.currentTimeMillis() - time) / 1000 > expireSeconds) { // 默认10分钟
					faultResult.remove(key + "_time");
				} else {
					log.info("not send message, expires time:" + expireSeconds);
					return;
				}
			}

			/* 故障记录入库 */
			MemFault memFault = createFindFaultEvent(host, port);

			/* 发送短信和邮件通知 */
			AppDesc appDesc = appDescDao.getByAppId(memFault.getAppId());
			if (appDesc != null) {
				List<String> mobiles = Arrays.asList(appDesc.getNotifyMobiles().split(","));
				FaultEvent faultEvent = new FaultEvent(memFault.getIp(), memFault.getPort(), memFault.getId());
				faultNotify.notifyFailure(appDesc.getNotifyEmails(), mobiles, faultEvent);
			}

		}

	}

	protected MemFault createFindFaultEvent(String host, int port) {
		MemFault memFault = memFaultDao.getByHostAndPort(host, port);
		// 从来没有记录或者记录已经处理了，是新的故障
		if (memFault == null || memFault.getStatus() == 1) {
			memFault = new MemFault();
			memFault.setIp(host);
			memFault.setPort(port);
			memFault.setStatus(0);
			memFault.setCreateTime(System.currentTimeMillis());
			// 根据host:port查询AppMemGroup信息
			AppMemGroup appMemGroup = appMemGroupDao.getByHostAndPort(host, port);
			memFault.setAppId(appMemGroup.getAppId());
			memFault.setGroupId(appMemGroup.getId());
			// 入库
			memFault.setId(memFaultDao.save(memFault));
		}

		return memFault;
	}

	
	private void sendRecoveryMessage(MemFault memFault) {

		/** 发送信息 */
		AppDesc appDesc = appDescDao.getByAppId(memFault.getAppId());
		if (appDesc != null) {
			List<String> mobiles = Arrays.asList(appDesc.getNotifyMobiles().split(","));
			FaultEvent faultEvent = new FaultEvent(memFault.getIp(), memFault.getPort(), memFault.getId());
			faultNotify.notifyRecovery(appDesc.getNotifyEmails(), mobiles, faultEvent);
		}
	}

	/**
	 * 查询暂时不发送通知的故障
	 */
	private void initHistoryFaultTime() {
		if (!ischeck) {
			List<MemFault> list = memFaultDao.getByGtTime(System.currentTimeMillis() - expireSeconds * 1000);
			for (MemFault memFault : list) {
				String key = getKey(memFault.getIp(), memFault.getPort());
				faultResult.put(key + "_time", memFault.getCreateTime());
			}
			ischeck = true;
		}
	}

	private String getKey(String host, Integer port) {
		return host + ":" + port;
	}

}
