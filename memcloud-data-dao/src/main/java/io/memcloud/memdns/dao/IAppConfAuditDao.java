package io.memcloud.memdns.dao;

import java.util.List;

import io.memcloud.memdns.dao.entry.AppConfAudit;

public interface IAppConfAuditDao {

	public AppConfAudit getByRecId(Long id);
	
	public List<AppConfAudit> getByAppId(Long id);
	
	public Long save(AppConfAudit appConfAudit);
	
	public void update(AppConfAudit appConfAudit);
	
	public void delete(Long id);
}
