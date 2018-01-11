package io.memcloud.memdns.dao;

import io.memcloud.memdns.dao.entry.MemHost;

public interface IMemHostDao {

	public MemHost getByIP(String ip);
	
	public MemHost get(Long ip);
	
	public Long save(MemHost memHost);
	
	public void update(MemHost memHost);
	
	public void delete(Long id);
	
}
