/**
 * 
 */
package io.memcloud.stats.dao;

import java.util.List;
import io.memcloud.memdns.dao.entry.MemFault;

/**
 * @author ganghuawang
 *
 */
public interface IMemFaultDao {

	public MemFault get(Long id);
	
	public List<MemFault> getByAppId(Long appId);
	
	public Long save(MemFault memFault);
		
	public void update(MemFault memFault);
	
	public void delete(Long id);
	
	public MemFault getByHostAndPort(String ip, Integer port);
	
	/**
	 * 最近时间内的故障
	 * @param timeMillis
	 * @return
	 */
	public List<MemFault> getByGtTime(Long timeMillis);
}
