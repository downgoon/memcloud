///*
// * Copyright (c) 2012 Sohu TV. All rights reserved.
// */
//package io.memcloud.stats;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.GregorianCalendar;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCollection;
//import com.mongodb.DBObject;
//import com.mongodb.MapReduceCommand;
//import com.mongodb.MapReduceOutput;
//
//import io.memcloud.driver.mongodb.MongodbDatasource;
///**
// * 
// * @author ganghuawang
// */
//public class TestMemcacheStat {
//
//	private ScheduledExecutorService scheduledService ;
//	
//	public TestMemcacheStat(){
//		scheduledService = Executors.newScheduledThreadPool(2);
//	}
//	
//	public void start(){
////		scheduledService.scheduleAtFixedRate(new GatherStatsThread(), 2, 30, TimeUnit.SECONDS);
//	}
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		TestMemcacheStat m = new TestMemcacheStat();
//		m.start();
////		m.mapReduce();
////		StatDBObject o=m.getCurrentStatDBObject();
////		System.out.println(o.get("_id"));
//	}
//	
//	public void test(){
//		DBCollection coll = MongodbDatasource.getInstance().getMongoDB().getCollection("coll_10.10.79.214:11211");
////		
////		StatDBObject mstat = new StatDBObject();
////		mstat.put("name", "ganghuawang");
////		mstat.put("age", 25);
////		coll.insert(mstat);
//		BasicDBObject indexDoc = new BasicDBObject();
//		indexDoc.put("datetime", 1);
//		coll.createIndex(indexDoc);
//		for(DBObject o : coll.getIndexInfo()){
//			System.out.println(o);
//		}
//		
////		System.out.println(System.currentTimeMillis());
////		BasicDBObject query = new BasicDBObject();
////		query.put("time", new BasicDBObject("$gt", 1338797338));
////		DBCursor cur = coll.find(query).sort(new BasicDBObject("time", -1)).limit(20);
////		while (cur.hasNext()) {
////			System.out.println(cur.next());
////		}
////		System.out.println(cur.size());
//		
//		
////		List<DBObject> indexs = coll.getIndexInfo();
////		System.out.println(indexs);
//		
////		List<MemcachedClusterClient> clientlist = memInstanceConnectionPool.getInstance().getClusterClient();
////		for (MemcachedClusterClient memcachedClusterClient : clientlist) {
////			MemcachedClient mc = memcachedClusterClient.getMemcachedClient();
////			new GatherThread(mc).start();
////		}
//	}
//	
//	public void mapReduce(){
//		//1338998373 2012-06-06 23:59:34
//		String date  = "2012-06-06";
//		GregorianCalendar gc = new GregorianCalendar();
//		try {
//			gc.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
//			System.out.println(1338981750);
//			long secord = gc.getTimeInMillis()/1000;
//			System.out.println(secord);
//			System.out.println((1338981750-secord)/60);
//			System.out.println((1338981750-secord)%60);
//			String m = "function(){ if(this.datetime.indexOf('"+date+"')>=0){" +
//			"hour=(this.time-"+secord+")/60;minute=(this.time-"+secord+")%60;if(minute%30==0){emit(this,minute)}}}";
//		String r= "function(data,value){return{ 'minute':value,'data': data };}";
//		System.out.println(m);
//		DBCollection coll = MongodbDatasource.getInstance().getMongoDB().getCollection("coll_10.10.79.214:11211");
//		MapReduceOutput out = coll.mapReduce(m, r, null, MapReduceCommand.OutputType.INLINE, null);
//		for ( DBObject obj : out.results() ) {
//		    System.out.println( obj );
//		}
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//
//
//}
