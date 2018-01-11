package io.memcloud.memdns.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import io.memcloud.memdns.business.IMemDNSPublisher;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppConf;
import io.memcloud.memdns.dao.entry.AppConfAudit;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;

public class DNSAction extends BaseAction {

	private static final long serialVersionUID = -3844295788960987982L;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;
	
	@Resource(name = "memDNSPublisher")
	private IMemDNSPublisher memDNSPublisher;
	
	/**
	 * 提供给AppClient做DNS刷新用的接口
	 * */
	@Override
	public String view()  {
		if (paramId == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("param appid required");
			return REST("nohtml");
		}
		AppConf appConf = memDNSPublisher.getAppConf(paramId);
		if (appConf == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("appid "+paramId+" not found");
			return REST("nohtml");
		}
		Map<String, Object> attachment = new LinkedHashMap<String, Object>();
		attachment.put("appId", appConf.getAppId());
		attachment.put("version", appConf.getVersion());
		attachment.put("groupText", appConf.getGroupText());
		attachment.put("shardNum", appConf.getShardNum());
		attachment.put("timestamp", System.currentTimeMillis());
		attachment.put("ttlSecond", 10);
		
		responseModel.setAttachment(attachment);
		return REST("nohtml");
	}

	/**
	 * 提供给管理员发布DNS
	 * */
	@Override
	public String create() {
		if (paramId == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("param appid required");
			return REST("create");
		}
		AppDesc appDesc = appDescDao.getByAppId(paramId);
		if (appDesc == null) {
			responseModel.setStatus(402);
			responseModel.setMessage("app not found");
			responseModel.setDebug("app id "+paramId+" not found");
			return REST("create");
		} 
		List<AppMemGroup> group = appMemGroupDao.getByAppId(paramId);
		if (group==null || group.size()<=0) {
			responseModel.setStatus(403);
			responseModel.setMessage("group not allocated");
			responseModel.setDebug("app id "+paramId+" group not allocated");
			return REST("create");
		}
		AppConfAudit audit = memDNSPublisher.publishAppConf(appDesc, 0L);
		responseModel.setStatus(200);
		responseModel.setMessage("succ");
		responseModel.setAttachment(audit);
		return REST("create");
	}
	
	/**
	 * 提供给管理员判断某个应用的集群分配情况与DNS发布情况是否一致
	 * @param	appid/paramId	应用ID
	 * @return	0、[400,500) 输入参数错误；1、否则一定输出：memGroupSize,appConfSize和sync （对象信息：appDesc,appConf和memGroup）
	 * */
	public String help() {
		if (paramId == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("param appid required");
			return REST("help");
		}
		AppDesc appDesc = appDescDao.getByAppId(paramId);
		if (appDesc == null) {
			responseModel.setStatus(402);
			responseModel.setMessage("app not found");
			responseModel.setDebug("app id "+paramId+" not found");
			return REST("help");
		} 
		final Long appId = paramId;
		Map<String, Object> attachment = new LinkedHashMap<String, Object>();
		responseModel.setAttachment(attachment);
		attachment.put("appDesc", appDesc);
		
		List<AppMemGroup> memGroup = appMemGroupDao.getByAppId(appId);
		attachment.put("memGroup", memGroup);
		
		AppConf appConf = memDNSPublisher.getAppConf(appId);
		attachment.put("appConf", appConf);
		
		
		int memGroupSize = ( memGroup==null ? 0 : memGroup.size());
		int appConfSize = ( appConf==null ? 0 : appConf.getShardNum());
		attachment.put("memGroupSize",  memGroupSize);
		attachment.put("appConfSize", appConfSize );
		attachment.put("sync", (memGroupSize==appConfSize));
		
		return REST("help");
	}

	@Override
	public String update()  {
		return create();
	}

	protected Long paramId;
	
	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}
}
