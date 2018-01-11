package io.memcloud.stats.business;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import io.memcloud.driver.mongodb.Constants;
import io.memcloud.driver.mongodb.MongodbDatasource;
import io.memcloud.driver.mongodb.StatDBObject;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemInstanceStatsCapture {
	
	private static final Logger log = Logger.getLogger(MemInstanceStatsCapture.class);
	
	@Resource(name = "mongodbDatasource")
	private MongodbDatasource mongodbDatasource;

	public void stat(MemcachedClient client) {
		
		try {
			// 对每个客户端执行统计命令
			Map<InetSocketAddress, Map<String, String>> stats = client.getStats(2000);
			for(InetSocketAddress add : stats.keySet()) {
				StatDBObject statDoc = new StatDBObject();
				Map<String, String> map = stats.get(add);
				for(String key : map.keySet()){
					// 如果是数字的话，存为MongoDB数字类型
					if(StringUtils.isNumeric(map.get(key))){
						statDoc.put(key, Long.parseLong(map.get(key)));
					}else {
						statDoc.put(key, map.get(key));
					}
					// 额外存入一个时间，用于统计查询
					statDoc.put(Constants.DATETIME, getDateStr());
				}
				// mongodb集合名
				String collName = Constants.COLL_PREFIX + add.toString().trim().replace("/", "");
				log.info(collName);
				// 存入mongodb
				mongodbDatasource.getDBCollection(collName).insert(statDoc);
			}
			
		} catch (MemcachedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 获得日期(yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	private String getDateStr(){
		GregorianCalendar gc = new GregorianCalendar();
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(gc.getTime());
	}


	public MongodbDatasource getMongodbDatasource() {
		return mongodbDatasource;
	}


	public void setMongodbDatasource(MongodbDatasource mongodbDatasource) {
		this.mongodbDatasource = mongodbDatasource;
	}
	
	
}
