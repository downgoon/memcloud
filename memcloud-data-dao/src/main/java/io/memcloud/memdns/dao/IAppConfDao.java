package io.memcloud.memdns.dao;

import io.memcloud.memdns.dao.entry.AppConf;

public interface IAppConfDao {

	public AppConf get(Long appId);
	
	public Long save(AppConf appConf);
	
	public void update(AppConf appConf);
	
	public void delete(Long id);
}
