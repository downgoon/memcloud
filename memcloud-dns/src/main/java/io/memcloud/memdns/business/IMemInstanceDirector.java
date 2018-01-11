package io.memcloud.memdns.business;

import java.util.List;

import io.memcloud.memdns.dao.ConcurrencyProblemException;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.memdns.dao.entry.MemInstance;
import io.memcloud.memdns.dao.entry.ScaleoutAppeal;

public interface IMemInstanceDirector {

	/** 引进新的MemInstance资源，方法是幂等的 */
	public MemInstance bringin(String memCmd);
	
	/**
	 * 为应用自动分配分片资源
	 * @param appDesc	应用
	 * @param	shardAmount		申请分片的数量
	 * @param	memSizeM	每个Memcache实例的内存大小
	 * @return	返回分配情况。NULL 表示没有足够资源，非空不一定表示完全申请好，可能申请5个分片，实际只成功3个分片。
	 * */
	public List<AppMemGroup> allocate(AppDesc appDesc,int shardAmount, int memSizeM) throws ConcurrencyProblemException;
	
	public List<AppMemGroup> allocate(AppDesc appDesc,int shardAmount, int memSizeM, ScaleoutAppeal appeal) throws ConcurrencyProblemException;
	
	
}
