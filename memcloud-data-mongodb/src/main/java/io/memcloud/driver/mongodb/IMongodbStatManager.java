/**
 * 
 */
package io.memcloud.driver.mongodb;

import java.util.List;

import com.mongodb.DBObject;

/**
 * @author ganghuawang
 *
 */
public interface IMongodbStatManager {

	/* 获取节点当前状态*/
	public StatDBObject getCurrentStat(String collName);
	
	/* 每日get次数趋势*/
	public List<StatDBObject> getDailyGetTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
	
	/* 每日set次数趋势*/
	public List<StatDBObject> getDailySetTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
	
	/* 每日hit次数趋势*/
	public List<StatDBObject> getDailyHitTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
	
	/* 每日miss次数趋势*/
	public List<StatDBObject> getDailyMissesTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
}
