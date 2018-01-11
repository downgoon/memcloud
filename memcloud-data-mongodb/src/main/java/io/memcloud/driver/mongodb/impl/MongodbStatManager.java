/**
 * 
 */
package io.memcloud.driver.mongodb.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import io.memcloud.driver.mongodb.Constants;
import io.memcloud.driver.mongodb.IMongodbStatManager;
import io.memcloud.driver.mongodb.MongodbDatasource;
import io.memcloud.driver.mongodb.StatDBObject;

/**
 * @author ganghuawang
 * 统计Memcached实例的运行状态
 */
public class MongodbStatManager implements IMongodbStatManager {

	private MongodbDatasource mongodbDatasource ;
	

	@Override
	public StatDBObject getCurrentStat(String collName) {
		DBCollection coll = mongodbDatasource.getDBCollection(collName);
		StatDBObject object = null;
		
		String dateStr = new SimpleDateFormat("yyyy-MM-dd HH").format(new Date());
		BasicDBObject query = new BasicDBObject();
		Pattern datePattern = Pattern.compile(dateStr+".*", Pattern.CASE_INSENSITIVE);
		query.put("datetime", datePattern);
		
		coll.setObjectClass(StatDBObject.class);
		DBCursor cur = coll.find(query).sort(new BasicDBObject("time", -1)).limit(1);
		while (cur.hasNext()) {
			return (StatDBObject)cur.next();
		}
		return object;
	}
	

	@Override
	public List<StatDBObject> getDailyGetTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit) {
		return getIncrementStatDBObject(query, collName, Constants.CMD_GET, timeUnit);
	}

	/* (non-Javadoc)
	 * @see io.memcloud.dao.IInstanceStatManager#getDailyHitTrendStat()
	 */
	@Override
	public List<StatDBObject> getDailyHitTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit) {
		return getIncrementStatDBObject(query, collName, Constants.GET_HITS, timeUnit);
	}

	/* (non-Javadoc)
	 * @see io.memcloud.dao.IInstanceStatManager#getDailyMissTrendStat()
	 */
	@Override
	public List<StatDBObject> getDailyMissesTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit) {
		return getIncrementStatDBObject(query, collName, Constants.GET_MISSES, timeUnit);
	}

	@Override
	public List<StatDBObject> getDailySetTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit) {
		return getIncrementStatDBObject(query, collName, Constants.TOTAL_ITEMS, timeUnit);
	}
	
	/**
	 * 
	 * @param date
	 * @param collName
	 * @param key
	 * @return
	 */
	private List<StatDBObject> getIncrementStatDBObject(DBObject query, String collName, String key, Constants.TimeUnit timeUnit){
		if(timeUnit == null) timeUnit = Constants.TimeUnit.MINUTES;
		
		List<DBObject> list = getDailyDBObject(query, collName, timeUnit );
		List<StatDBObject> result = new ArrayList<StatDBObject>();
		DBObject pre = null;
		//计数器
		int i = 0;
		for(DBObject object : list){
			if(i > 0){
				StatDBObject stat = new StatDBObject();
				//增量
				long incre = Long.parseLong(object.get(key)+"") - Long.parseLong(pre.get(key)+"");
				stat.put(Constants.STAT_DATA_KEY, incre);
				//时间点
				stat.put(Constants.DATETIME, object.get(Constants.DATETIME));
				result.add(stat);
			}
			pre = object;
			i++;
		}
		return result;
	}
	
	/**
	 * 获取到Mongodb的数据记录
	 * @param collName
	 * @return
	 */
	private List<DBObject> getDailyDBObject(DBObject query, String collName, Constants.TimeUnit timeUnit){
		DBCollection coll = mongodbDatasource.getDBCollection(collName);
		List<DBObject> list = new ArrayList<DBObject>();
		int i=0;

		coll.setObjectClass(StatDBObject.class);
		DBCursor cur = coll.find(query);
		int totalSize = cur.size();
		while(cur.hasNext()){
			i++;
			DBObject dbObj = cur.next();
			//第一条数据不用过滤
			if(i != 1){
				Date datetime = null;
				try {
					datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(String.valueOf(dbObj.get(Constants.DATETIME)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
					int minuteNow = getMinuteOfDay(datetime);
					//取区间时间节点的数据
					if (minuteNow % timeUnit.getValue() == 0) {
						list.add(dbObj);
					} else if (i == totalSize ) {		
						if(list.size() == 1){ /* 只是区间的一部分，需要再取最近的一条数据*/
							list.add(dbObj);
						}else if(timeUnit == Constants.TimeUnit.HOUR && minuteNow > (23*60+58)){ /*按小时显示，需要23:59的数据*/
							list.add(dbObj);
						}
					}
			}else{
				list.add(dbObj);
			}
		}
		return list;
	}
	
	/**
	 * 得到是一天的第多少分钟
	 * @param date
	 * @return
	 */
	private int getMinuteOfDay(Date date){
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(Calendar.HOUR_OF_DAY)*60 + gc.get(Calendar.MINUTE);
	}


	public MongodbDatasource getMongodbDatasource() {
		return mongodbDatasource;
	}


	public void setMongodbDatasource(MongodbDatasource mongodbDatasource) {
		this.mongodbDatasource = mongodbDatasource;
	}
	

}
