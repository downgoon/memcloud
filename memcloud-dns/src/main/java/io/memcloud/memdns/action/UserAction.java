package io.memcloud.memdns.action;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import io.memcloud.cas.IAccount;
import io.memcloud.memdns.dao.IUserDao;
import io.memcloud.memdns.dao.entry.User;

public class UserAction extends BaseAction {

	private static final long serialVersionUID = 1005494871655875735L;
	
	@Resource(name = "userDao")
	private IUserDao userDao;
	
	/**
	 * 条件：登录
	 * 输入：无
	 * 输出：登录者的账号信息
	 * */
	@Override
	public String index() {
		IAccount acc = loginedUser();
		User user = userDao.get(acc.getUserId());
		if (user == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("logined user not found");
			return REST("index");
		}
		Map<String,Object> attachment = new LinkedHashMap<String, Object>();
		attachment.put("uid", user.getId());
		attachment.put("name", user.getName());
		attachment.put("mobile", user.getMobile());
		attachment.put("email", user.getEmail());
		responseModel.setAttachment(attachment);
		return REST("index");
	}
	
	@Override
	public String view() {
		if (paramId==null) {
			responseModel.setStatus(401);
			responseModel.setMessage("userid required");
			return REST("view");
		}
		User user = userDao.get(paramId);
		if (user == null) {
			responseModel.setStatus(404);
			responseModel.setMessage("user not found");
			return REST("view");
		}
		Map<String,Object> attachment = new LinkedHashMap<String, Object>();
		attachment.put("uid", user.getId());
		attachment.put("name", user.getName());
		attachment.put("mobile", user.getMobile());
		attachment.put("email", user.getEmail());
		responseModel.setAttachment(attachment);
		return REST("view");
	}
	
	protected Long paramId;
	
	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}

}
