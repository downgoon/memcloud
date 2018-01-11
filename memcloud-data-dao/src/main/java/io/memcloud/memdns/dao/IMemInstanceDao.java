
package io.memcloud.memdns.dao;

import java.util.List;

import io.memcloud.memdns.dao.entry.MemInstance;
import io.memcloud.memdns.dao.entry.MemInstancePeer;

public interface IMemInstanceDao {

	public MemInstance get(String ip, Integer port);
	
	public MemInstance get(Long id);
	
	public MemInstance getByRepc(String ip, Integer repcPort); 
	
	public Long save(MemInstance memInstance);
	
	public void update(MemInstance memInstance);
	
	public void delete(Long id);
	
	public List<MemInstance> get();
	
	public int getUnusedPeerCount(int memSizeM);
	
	public List<MemInstancePeer> getUnusedPeer(int offset, int pageSize, int memSizeM);
	
	/**
	 * 将未被占用的列表修改成已经占用了
	 * @return	返回更新的个数
	 * */
	public int allocUnusedPeer(List<Long> instanceIDs);
	
	public List<MemInstance> getAll();
	
}


