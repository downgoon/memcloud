package io.memcloud.memdns.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import io.memcloud.cas.IAccount;
import io.memcloud.memdns.business.IMemInstanceDirector;
import io.memcloud.memdns.dao.ConcurrencyProblemException;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IScaleoutAppealDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.memdns.dao.entry.ScaleoutAppeal;

public class ScaleAction extends BaseAction {

	private static final long serialVersionUID = 1095974495666465095L;
	
	@Resource(name = "scaleoutAppealDao")
	private IScaleoutAppealDao scaleoutAppealDao;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "memInstanceDirector")
	private IMemInstanceDirector memInstanceDirector;
	
	@Override
	public String create()  {
		IAccount acc = loginedUser();
		if (paramId == null ) {
			responseModel.setStatus(401);
			responseModel.setMessage("appid required");
			return REST("create");
		}
		getHttpRequest().setAttribute("paramId", paramId);
		
		AppDesc appDesc = appDescDao.getByAppId(paramId);
		if (appDesc == null ) {
			responseModel.setStatus(404);
			responseModel.setMessage("app "+paramId+" not found");
			return REST("create");
		}
		getHttpRequest().setAttribute("appDesc", appDesc);//jsp表现层处理
		if ( ! acc.getUserId().equals(appDesc.getOwnerUid())) {
			responseModel.setStatus(405);
			responseModel.setMessage("app "+paramId+" not yours");
			responseModel.setDebug("越主代庖，多管闲事~~：别提别人的应用申请扩容！");
			return REST("create");
		}
		
		if (shard == null || shard < 1 || shard > 20) {
			responseModel.setStatus(401);
			responseModel.setMessage("shard required and scope: [1,20]");
			return REST("create");
		}
		if (mem == null || mem < 256 || mem > 2048) {
			responseModel.setStatus(401);
			responseModel.setMessage("mem required and scope: [256,2048]");
			return REST("create");
		}
		
		/* 防止下由于网络原因导致的二次下单
		 * 1、表单中设置隐藏字段，携带一个UUID之类的，作为下单方的订单号。
		 * 2、简单处理下，一个用户不能在5分钟内提交两次，除非先把之前的删除。这种实现手段依然不满足高并发环境的要求。
		 * */
		Long tmNow = System.currentTimeMillis();
		Long tmAgo5Min = (tmNow - 1000L*60*5); 
		List<ScaleoutAppeal> recent = scaleoutAppealDao.getByUid(acc.getUserId(), tmAgo5Min, tmNow);
		if ( recent!=null && recent.size() > 0) {
			responseModel.setStatus(406);
			responseModel.setMessage("created already or create another one 5 min later");
			responseModel.setDebug("请5分钟之后再提交新的扩容需求。");
			return REST("create");
		}
		
		ScaleoutAppeal sa = new ScaleoutAppeal();
		sa.setAppId(appDesc.getId());
		sa.setAppName(appDesc.getName());
		sa.setUserId(acc.getUserId());
		sa.setStatus(ScaleoutAppeal.STATUS_WAITING);
		sa.setShardNum(shard);
		sa.setMemSize(mem);
		sa.setCreateTime(System.currentTimeMillis());
		scaleoutAppealDao.save(sa);
		
		responseModel.setStatus(200);
		responseModel.setMessage("succ");
		responseModel.setAttachment(sa);
		return REST("create");
	}

	/**
	 * 当前登录用户申请过的扩容请求列表
	 * */
	@Override
	public String index() {
		IAccount acc = loginedUser();
		List<ScaleoutAppeal> saList = scaleoutAppealDao.getByUid(acc.getUserId());
		responseModel.setAttachment(saList);
		return REST("index");
	}
	
	/** 管理员查批（展示待审列表） */
	public String adminidx() {
		short status = ScaleoutAppeal.STATUS_WAITING;
		if ((ScaleoutAppeal.STATUS_PASSED+"").equals(getParam("status"))) {
			status = ScaleoutAppeal.STATUS_PASSED;
		} else if ((ScaleoutAppeal.STATUS_REJECT+"").equals(getParam("status"))) { 
			status = ScaleoutAppeal.STATUS_REJECT;
		}
		List<ScaleoutAppeal> saList = scaleoutAppealDao.getStatusRecent(status);
		responseModel.setAttachment(saList);
		return REST("adminidx");
	}
	
	/**
	 * 管理员审批某个具体的申请
	 * @param	paramId   审核申请记录的ID
	 * @param	action	可选，默认是空，表示查询。reject 表示驳回拒批； accept 表示通过审批，但是数量不一定跟用户申请的一样。
	 * @param	shard	条件参数，当action=accept时，必选，表示实际审批通过时的分片数
	 * @param	mem		条件参数，当action=accept时，必选，表示实际审批通过时的每个分片的内存容量
	 * @return	attachment	
	 * 			0、400之类的响应，表示输入参数有问题；
	 * 			1、appeal 必选；2、action 必选  edit|accept|reject
	 * */
	public String adminedit() {
		if (paramId == null ) {
			responseModel.setStatus(401);
			responseModel.setMessage("appeal id required");
			return REST("adminedit");
		}
		ScaleoutAppeal sa = scaleoutAppealDao.get(paramId);
		if (sa == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("record not found");
			return REST("adminedit");
		}
		
		Map<String, Object> attachment = new LinkedHashMap<String, Object>();
		responseModel.setAttachment(attachment);
		attachment.put("appeal", sa);
		
		String action = getParam("action");
		if (StringUtils.isEmpty(action)) {//表示提审（或者说查询），展示页面进行交互。
			responseModel.setStatus(244);
			responseModel.setMessage("scale view info");
			responseModel.setDebug("没有明确是审批通过还是拒绝，理解为状态查询");
			attachment.put("action", "edit");
			attachment.put("actionNum", 0);
			responseModel.setAttachment(attachment);
			return REST("adminedit");
		}
		int status = ScaleoutAppeal.STATUS_REJECT;
		if (StringUtils.endsWithIgnoreCase(action, "accept")) {
			status = ScaleoutAppeal.STATUS_PASSED;
			attachment.put("action", "accept");
			attachment.put("actionNum", 1);
			
		} else if (StringUtils.endsWithIgnoreCase(action, "reject")) {
			status = ScaleoutAppeal.STATUS_REJECT;
			attachment.put("action", "reject");
			attachment.put("actionNum", -1);
			
		} else {
			responseModel.setStatus(401);
			responseModel.setMessage("action arg err");
			responseModel.setDebug("action参数值：accept | reject | 空");
			return REST("adminedit");
		}
		
		if (status == ScaleoutAppeal.STATUS_REJECT) {//拒批
			sa.setStatus(ScaleoutAppeal.STATUS_REJECT);
			sa.setPassedTime(System.currentTimeMillis());
			sa.setPassedShard(0);
			sa.setPassedMem(0);
			scaleoutAppealDao.update(sa);
			return REST("adminedit");
		}
			
		//通过批准
		if (shard==null || mem==null || !(shard<=20  && shard>=1) || !(mem<=2048 && mem>=256)) {
			responseModel.setStatus(402);
			responseModel.setMessage("shard or mem arg err");
			responseModel.setDebug("shard: [1,20] and mem: [256,2048]");
			return REST("adminedit");
		}
		AppDesc appDesc = appDescDao.getByAppId(sa.getAppId());
		List<AppMemGroup> group = null;
		try {
			group = memInstanceDirector.allocate(appDesc, shard, mem, sa);//资源分配和待审记录的更新在同一个事务里面
		} catch (ConcurrencyProblemException e) {
			log.warn("Concurrency Problem Detected",e);
			responseModel.setStatus(505);
			responseModel.setMessage("Concurrency Problem Detected");
			responseModel.setDebug("云资源出现并发竞争，请稍后重试");
			return REST("adminedit");
		}
		if (group==null) {//资源池的资源不够
			responseModel.setStatus(504);
			responseModel.setMessage("mem sharding not enough");
			responseModel.setDebug("云资源不足，不够分配："+mem+"M*"+shard);
			return REST("adminedit");
		}
		
		return REST("adminedit");
	}
	
	@Override
	public String view()  {
		if (paramId == null ) {
			responseModel.setStatus(401);
			responseModel.setMessage("appid required");
			return REST("create");
		}
		IAccount acc = loginedUser();
		ScaleoutAppeal sa = scaleoutAppealDao.get(paramId);
		if (sa == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("record not found");
			return REST("create");
		}
		if ( ! sa.getUserId().equals(acc.getUserId())) {
			responseModel.setStatus(407);
			responseModel.setMessage("no permission");
			responseModel.setDebug("不能查看别人的申请记录");
			return REST("create");
		}
		responseModel.setAttachment(sa);
		return REST("view");
	}

	@Override
	public String remove()  {
		if (paramId == null ) {
			responseModel.setStatus(401);
			responseModel.setMessage("appid required");
			return REST("remove");
		}
		IAccount acc = loginedUser();
		ScaleoutAppeal sa = scaleoutAppealDao.get(paramId);
		if (sa == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("record not found");
			return REST("remove");
		}
		if ( ! sa.getUserId().equals(acc.getUserId())) {
			responseModel.setStatus(407);
			responseModel.setMessage("no permission");
			responseModel.setDebug("不能删除别人的申请记录");
			return REST("remove");
		}
		if (sa.getStatus() != ScaleoutAppeal.STATUS_WAITING) {
			responseModel.setStatus(408);
			responseModel.setMessage("verified appeal cann't be removed");
			responseModel.setDebug("不能删除已经审核过的申请");
			return REST("remove");
		}
		scaleoutAppealDao.delete(sa.getId());
		responseModel.setMessage("remove succ");
		responseModel.setAttachment(sa);
		return REST("remove");
	}
	
	protected Long paramId;
	
	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}
	
	private Integer shard;
	
	private Integer mem;

	public void setShard(Integer shard) {
		this.shard = shard;
	}

	public void setMem(Integer mem) {
		this.mem = mem;
	}
	
}
