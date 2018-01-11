package io.memcloud.memdns.dao;

import java.util.List;

import io.memcloud.memdns.dao.entry.ScaleoutAppeal;

public interface IScaleoutAppealDao {

	public ScaleoutAppeal get(Long id);
	
	public List<ScaleoutAppeal> getByUid(Long uid);
	
	public List<ScaleoutAppeal> getByUid(Long userId, Long beginTime, Long endTime);
	
	public List<ScaleoutAppeal> getStatusRecent(short status);
	
	public List<ScaleoutAppeal> getStatusList(short status, Long beginTime, Long endTime );
	
	public Long save(ScaleoutAppeal scaleAppeal);
	
	public void update(ScaleoutAppeal scaleAppeal);
	
	public void delete(Long id);
	
	
}
