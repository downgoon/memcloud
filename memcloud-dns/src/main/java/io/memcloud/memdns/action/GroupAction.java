package io.memcloud.memdns.action;

import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.business.IMemInstanceDirector;
import io.memcloud.memdns.dao.ConcurrencyProblemException;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;

public class GroupAction extends BaseAction  {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "memInstanceDirector")
	private IMemInstanceDirector memInstanceDirector;
	
	
	/**
	 * @param appid 应用ID
	 * @return	应用的Memcached集群
	 * */
	@Override
	public String view() {
		if (paramId == null ) {
			responseModel.setStatus(RC_ERR_ARG);
			responseModel.setMessage("appid required");
			return REST("view");
		}
		List<AppMemGroup> shards = appMemGroupDao.getByAppId(paramId);
		responseModel.setAttachment(shards);
		return REST("view");
	}

	/**
	 * 为某个应用分配集群，包括首次分配和追加分配。
	 * */
	@Override
	public String create()  {
		if (paramId == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("arg format error");
			responseModel.setDebug("AppID缺少");
			return REST("create");
		}
		StringBuffer errinfo = new StringBuffer();
		if (shard == null || !(shard<=5 && shard>=1)) {
			errinfo.append("shard scope: [").append(1).append(",").append(5).append("];");
		}
		if (mem == null || !(mem<=2048 && mem>=256)) {
			errinfo.append("mem scope: [").append(256).append(",").append(2048).append("];");
		}
		if (errinfo!=null && errinfo.length() > 0) {
			errinfo.delete(errinfo.length()-1, errinfo.length());
			responseModel.setStatus(401);
			responseModel.setMessage("arg format error");
			responseModel.setDebug(errinfo.toString());
			return REST("create");
		}
		AppDesc appDesc = appDescDao.getByAppId(paramId);
		if (appDesc == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("app not found");
			responseModel.setDebug("AppID "+paramId +" 没有找到！");
			return REST("create");
		}
		
		List<AppMemGroup> group = null;
		try {
			group = memInstanceDirector.allocate(appDesc, shard, mem);
		} catch (ConcurrencyProblemException e) {
			log.warn("shard allocated failure for app "+appDesc.getName()+"("+appDesc.getId()+") due to ConcurrencyProblemException", e);
		}
		int allocated = 0;
		if (group != null) {
			allocated = group.size();
		}
		if (allocated < shard) {
			responseModel.setStatus(201);
			responseModel.setMessage("succ partially");
		} else {
			responseModel.setStatus(200);
			responseModel.setMessage("succ completely");
		}
		responseModel.setAttachment(group);
		responseModel.setDebug("分配了"+allocated+"个分片，但需要等待管理员审核并发布到mem-dns。");
		return REST("create");
	}

	private Integer shard;
	
	private Integer mem;

	public Integer getShard() {
		return shard;
	}

	public void setShard(Integer shard) {
		this.shard = shard;
	}

	public Integer getMem() {
		return mem;
	}

	public void setMem(Integer mem) {
		this.mem = mem;
	}
	
	protected Long paramId;
	
	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}
	
}
