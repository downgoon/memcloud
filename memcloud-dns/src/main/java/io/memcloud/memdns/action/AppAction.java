package io.memcloud.memdns.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import io.memcloud.cas.IAccount;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.model.DataDict;

public class AppAction extends BaseAction {

	private static final long serialVersionUID = 1005494871655875735L;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao; 
	
	
	/**
	 * 我的应用列表：查看当前登录者所拥有的所有应用的列表
	 * */
	@Override
	public String index() {
		IAccount acc = loginedUser();
		List<AppDesc> myApps = appDescDao.getByUid(acc.getUserId());
		responseModel.setAttachment(myApps);
		
		if (myApps!=null && myApps.size()>0) {
			List<Long> appIdList = new ArrayList<Long>(myApps.size());
			for (AppDesc appDesc : myApps) {
				appIdList.add(appDesc.getId());
			}
			LinkedHashMap<Long,Long> shardCountMap = appMemGroupDao.shardCount(appIdList);
			getHttpRequest().setAttribute("shardCountMap",shardCountMap );
		}
		return REST("index");
	}

	/**
	 * 查看某个应用的详细信息
	 * */
	@Override
	public String view()  {
		if (paramId == null ) {
			responseModel.setStatus(RC_ERR_ARG);
			responseModel.setMessage("appid required");
			return REST("view");
		}
		AppDesc appDesc = appDescDao.getByAppId(paramId);
		if (appDesc==null) {
			responseModel.setStatus(404);
			responseModel.setMessage("appid "+paramId+" not found");
			return REST("view");
		}
		responseModel.setAttachment(appDesc);
		return REST("view");
	}
	
	/** 
	 * 创建某个应用  
	 * 注意：站在用户的角度希望创建应用并同时分配集群，这个逻辑应该依靠前端Wizard引导，而不应该在一次交互中完成。 
	 * */
	@Override
	public String create() {
		IAccount acc = loginedUser();
		
		String name = getParam("name");
		String descr = getParam("descr");
		String notifyEmails = getParam("emails");
		String notifyMobiles = getParam("mobiles");
		
		if ( name == null && descr == null && notifyEmails == null && notifyMobiles == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("name and descr required");
			responseModel.setDebug("至少需要name和descr参数");
			return REST("create");
		} 

		StringBuffer errinfo = new StringBuffer();
		if (StringUtils.isEmpty(name) || !name.matches(DataDict.AppNameRegex)) {
			errinfo.append("name dismatch: ").append(DataDict.AppNameRegex).append(";");
		}
		if (StringUtils.isEmpty(descr) || !descr.matches(DataDict.AppDescRegex)) {
			errinfo.append("descr dismatch: ").append(DataDict.AppDescRegex).append(";");
		} 
		if (StringUtils.isNotEmpty(descr)) {
			int descrSize = descr.length();
			descr = HtmlUtils.htmlEscape(descr);
			if (descrSize != descr.length()) {
				errinfo.append("descr contains many HTML flag ;");
			}
		}
		
		if (StringUtils.isNotEmpty(notifyEmails) && !notifyEmails.matches(DataDict.EmailListRegex)) {
			errinfo.append("emails dismatch: ").append(DataDict.EmailListRegex).append(";");
		}
		if (StringUtils.isNotEmpty(notifyEmails) && notifyEmails.length()>128) {
			errinfo.append("emails too long: ").append(128).append(";");
		}
		if (StringUtils.isNotEmpty(notifyMobiles) && !notifyMobiles.matches(DataDict.MobileListRegex)) {//
			errinfo.append("mobiles dismatch: ").append(DataDict.MobileListRegex).append(";");
		}
		if (StringUtils.isNotEmpty(notifyMobiles) && notifyMobiles.length()>128) {//可选
			errinfo.append("mobiles too long: ").append(128).append(";");
		}
		
		if (errinfo!=null && errinfo.length() > 0) {
			errinfo.delete(errinfo.length()-1, errinfo.length());
			responseModel.setStatus(401);
			responseModel.setMessage("arg format error");
			responseModel.setDebug(errinfo.toString());
			return REST("create");
		}
		
		AppDesc appFound = appDescDao.getByAppName(name);
		if (appFound != null) {
			if (appFound.getOwnerUid()==acc.getUserId() && (System.currentTimeMillis()-appFound.getCreateTime() < 1000L*60*2)) {
				responseModel.setStatus(201);//同一个用户，在短短2分钟内再次提交，理解为网络故障，支持幂等
				responseModel.setMessage("succ already");
				responseModel.setDebug("应用："+name+" 已保存成功！");
			} else {
				responseModel.setStatus(404);//应用名称已被他人占用
				responseModel.setMessage("app name exist");
				responseModel.setDebug("应用："+name+" 已被占用！");
			}
			responseModel.setAttachment(appFound);
			return REST("create");
		}
		
		AppDesc appDesc = new AppDesc();
		appDesc.setName(name);
		appDesc.setDescr(descr);
		appDesc.setStatus(0);
		appDesc.setCreateTime(System.currentTimeMillis());
		appDesc.setOwnerUid(acc.getUserId());
		if (StringUtils.isNotEmpty(notifyEmails)) {
			appDesc.setNotifyEmails(notifyEmails);
		}
		if (StringUtils.isNotEmpty(notifyMobiles)) {
			appDesc.setNotifyMobiles(notifyMobiles);
		}
		appDescDao.save(appDesc);
		responseModel.setStatus(200);
		responseModel.setMessage("succ");
		responseModel.setAttachment(appDesc);
		return REST("create");
	}

	
	@Override
	public String update() {
		String name = getParam("name");
		String descr = getParam("descr");
		String notifyEmails = getParam("emails");
		String notifyMobiles = getParam("mobiles");

		StringBuffer errinfo = new StringBuffer();
		if (StringUtils.isNotEmpty(name) && !name.matches(DataDict.AppNameRegex)) {
			errinfo.append("name dismatch: ").append(DataDict.AppNameRegex).append(";");
		}
		if (StringUtils.isNotEmpty(descr) && !descr.matches(DataDict.AppDescRegex)) {
			errinfo.append("descr dismatch: ").append(DataDict.AppDescRegex).append(";");
		}
		if (StringUtils.isNotEmpty(notifyEmails) && !notifyEmails.matches(DataDict.EmailListRegex)) {
			errinfo.append("emails dismatch: ").append(DataDict.EmailListRegex).append(";");
		}
		if (StringUtils.isNotEmpty(notifyEmails) && notifyEmails.length()>128) {
			errinfo.append("emails too long: ").append(128).append(";");
		}
		if (StringUtils.isNotEmpty(notifyMobiles) && !notifyMobiles.matches(DataDict.MobileListRegex)) {//
			errinfo.append("mobiles dismatch: ").append(DataDict.MobileListRegex).append(";");
		}
		if (StringUtils.isNotEmpty(notifyMobiles) && notifyMobiles.length()>128) {//可选
			errinfo.append("mobiles too long: ").append(128).append(";");
		}
		
		if (errinfo!=null && errinfo.length() > 0) {
			errinfo.delete(errinfo.length()-1, errinfo.length());
			responseModel.setStatus(401);
			responseModel.setMessage("arg format error");
			responseModel.setDebug(errinfo.toString());
			return REST("update");
		}

		if (StringUtils.isEmpty(name) && StringUtils.isEmpty(descr) && StringUtils.isEmpty(notifyMobiles) && StringUtils.isEmpty(notifyEmails)) {
			responseModel.setStatus(401);
			responseModel.setMessage("arg format error");
			responseModel.setDebug("没有提交需要修改的参数值");
			return REST("update");
		}
		
		if (paramId == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("arg format error");
			responseModel.setDebug("AppID缺少");
			return REST("update");
		}
		AppDesc appFound = appDescDao.getByAppId(paramId);
		if (appFound == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("app not found");
			responseModel.setDebug("AppID "+paramId +" 没有找到！");
			return REST("update");
		}
		boolean hasDiff = false;
		if (StringUtils.isNotEmpty(name) && !StringUtils.equals(name, appFound.getName())) {
			hasDiff = true;
			appFound.setName(name);
		}
		if (StringUtils.isNotEmpty(descr) && !StringUtils.equals(descr, appFound.getDescr())) {
			hasDiff = true;
			appFound.setDescr(descr);
		}
		if (StringUtils.isNotEmpty(notifyEmails) && !StringUtils.equals(notifyEmails, appFound.getNotifyEmails())) {
			hasDiff = true;
			appFound.setNotifyEmails(notifyEmails);
		}
		if (StringUtils.isNotEmpty(notifyMobiles) && !StringUtils.equals(notifyMobiles, appFound.getNotifyMobiles())) {
			hasDiff = true;
			appFound.setNotifyMobiles(notifyMobiles);
		}
		if (hasDiff) {
			appDescDao.update(appFound);
		} 
		
		responseModel.setStatus(200);
		responseModel.setMessage("succ update");
		responseModel.setDebug(hasDiff?"修改成功":"内容没变，不需要修改");
		responseModel.setAttachment(appFound);
		return REST("update");
	}

	protected Long paramId;
	
	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}
	
}
