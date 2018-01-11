package io.memcloud.driver.mongodb;

public interface Constants {

	//mongodb集合名前缀
	public static final String COLL_PREFIX = "coll_";
	
	//memcached状态信息字段
	public static final String BYTES = "bytes";
	public static final String MAX_BYTES = "limit_maxbytes";
	public static final String BYTES_READ = "bytes_read";
	public static final String BYTES_WRITTEN = "bytes_written";
	public static final String CUEE_CONNECTIONS = "curr_connections";
	public static final String TOTAL_CONNECTIONS = "total_connections";
	public static final String GET_MISSES = "get_misses";
	public static final String GET_HITS = "get_hits";
	public static final String DELETE_MISSES = "delete_misses";
	
	public static final String STAT_DATA_KEY = "stat_key";  //统计的结果
	public static final String DATETIME = "datetime";
	public static final String CMD_GET = "cmd_get";
	public static final String CMD_SET = "cmd_set";
	public static final String CAS_HITS = "cas_hits";
	public static final String CAS_MISSES = "cas_misses";
	public static final String INCR_HITS = "incr_hits";
	public static final String INCR_MISSES = "incr_misses";
	public static final String DECR_HITS = "decr_hits";
	public static final String DECR_MISSES = "decr_misses";
	public static final String CURR_ITEMS = "curr_items";
	public static final String TOTAL_ITEMS = "total_items";
	
	public enum TimeUnit { 
		MINUTES {
			@Override
			public int getValue(){ return 1;}
		},
		MINUTES_5 {
			@Override
			public int getValue(){ return 5;}
		},
		MINUTES_10 {
			@Override
			public int getValue(){ return 10;}
		},
		MINUTES_30 {
			@Override
			public int getValue(){ return 30;}
		},
		HOUR {
			@Override
			public int getValue(){ return 60;}
		},
		DAY {
			@Override
			public int getValue(){ return 60*24;}
		},
		DEFAULT {
			@Override
			public int getValue(){ return 5;}
		};
		public abstract int getValue();
	}
	
}
