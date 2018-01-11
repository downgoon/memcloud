package io.memcloud.stats.model;

import java.util.Date;

import com.github.downgoon.jresty.commons.utils.DateUtil;
import com.github.downgoon.jresty.commons.utils.HumanizedFormator;

/**
 * REFER:  http://blog.163.com/czg_e/blog/static/46104561201132852042812/ 
 * */
public class MemStatSummary {

	private String version;
	
	/** memcached运行的秒数 */
	private long uptimeSecond;
	
	/** 系统当前时间*/
	private long currTimeSecond;
	
	
	
	/* 第一部分  关于吞吐量和命中率 */
	
	/** get指令执行次数  cmd_get/uptime 结果是平均每秒请求缓存的次数（结果值越大，说明Memcached的利用率越高，站点的访问量大，如果太低，用文件系统缓存就可以了，根本不会体现出使用memcached的强大性能）*/
	private long cmd_get;

	/** 执行get时命中的次数  所谓的命中率 = get_hits/cmd_get * 100%。*/
	private long get_hit;
	
	/** 执行get时未命中的次数*/
	private long get_mis;
	
	/**set指令执行的次数 (是设置key=>value的次数。整个memcached是个大hash，用cmd_get没有找到的内容，就会调用一下cmd_set写进缓存里。)*/
	private long cmd_set;
	
	/* 第二部分  关于内存使用率（是否需要扩容）*/
	
//	STAT curr_items 8311                       服务器当前存储的内容数量
//	STAT total_items 255141                    服务器启动以来存储过的内容总数
//	STAT limit_maxbytes 134217728              服务器在存储时被允许使用的字节总数,分配的内存数（字节）,这个是128M
//	STAT bytes 4875895                         服务器当前存储内容所占用的字节数
	
	private long currItems;
	private long totalItems;
	
	private long usedBytes;//STAT bytes 4875895                         服务器当前存储内容所占用的字节数
	private long totalBytes;//STAT limit_maxbytes 134217728              服务器在存储时被允许使用的字节总数,分配的内存数（字节）,这个是128M
	
	
	/* 第三部分  关于连接数（客户端是否太多了）*/
	private long currConns;
	private long totalConns;
	
	/* 第四部分  关于网络带宽*/
//	STAT bytes_read 217230173                  服务器从网络读取到的总字节数
//	STAT bytes_written 246524464               服务器向网络发送的总字节数
	private long readBytes;
	private long writeBytes;

	public double getUsedPercentage () {
		return (this.usedBytes+0.0) / this.totalBytes;
	}
	public double getHitPercentage() {
		if (cmd_get == 0) {
			return 0.0;
		}
		return (this.get_hit+0.0) / this.cmd_get;
	}
	
	public double getReadRate() {
		return (readBytes+0.0) / uptimeSecond;
	}
	
	public double getWriteRate() {
		return (writeBytes+0.0) / uptimeSecond;
	}
	
	public double getIORate() {
		return (readBytes+writeBytes+0.0) / uptimeSecond;
	}
	
	public long getReadBytes() {
		return readBytes;
	}

	public void setReadBytes(long readBytes) {
		this.readBytes = readBytes;
	}

	public long getWriteBytes() {
		return writeBytes;
	}

	public void setWriteBytes(long writeBytes) {
		this.writeBytes = writeBytes;
	}

	public long getExpireConns() {
		return totalConns - currConns;
	}
	
	public long getCurrConns() {
		return currConns;
	}

	public void setCurrConns(long currConns) {
		this.currConns = currConns;
	}

	public long getTotalConns() {
		return totalConns;
	}

	public void setTotalConns(long totalConns) {
		this.totalConns = totalConns;
	}

	public long getCurrItems() {
		return currItems;
	}

	public void setCurrItems(long currItems) {
		this.currItems = currItems;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public long getExpireItems() {
		return totalItems - currItems;
	}
	
	public long getFreeBytes() {
		return totalBytes - usedBytes;
	}

	public long getUsedBytes() {
		return usedBytes;
	}

	public void setUsedBytes(long userBytes) {
		this.usedBytes = userBytes;
	}

	public long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}

	/*** GET命令执行的次数 */
	public long getGetCount() {
		return this.cmd_get;
	}
	
	public double getGetRate() {
		return (this.cmd_get+0.0) / uptimeSecond;
	}
	
	/** 命中次数（GET并且命中的次数）*/
	public long getHitCount() {
		return this.get_hit;
	}
	
	public double getHitRate() {
		return (get_hit+0.0) / uptimeSecond;
	}
	
	/** 未命中次数（GET并且未命中的次数）*/
	public long getMisCount() {
		return this.get_mis;
	}
	
	public double getMisRate() {
		return (get_mis+0.0) / uptimeSecond;
	}
	
	/**执行SET命令的次数*/
	public long getSetCount() {
		return this.cmd_set;
	}
	
	public double getSetRate() {
		return (cmd_set+0.0) / uptimeSecond;
	}
	
	public static String prettySecond(long second, String pattern) {
		// return DateUtil.format(new Date(second*1000L),pattern);
		return DateUtil.format(new Date(second*1000L),pattern);
	}
	
	public static String humanSecond(long second) {
		return HumanizedFormator.seconds(second);
	}
	
	public static String humanBytes(double bytes) {
		return humanBytes((long)bytes);
	}
	
	public static String humanBytes(long bytes) {
		return HumanizedFormator.bytes(bytes);
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getUptimeSecond() {
		return uptimeSecond;
	}

	public void setUptimeSecond(long uptimeSecond) {
		this.uptimeSecond = uptimeSecond;
	}

	public long getCmd_get() {
		return cmd_get;
	}

	public void setCmd_get(long cmd_get) {
		this.cmd_get = cmd_get;
	}

	public long getGet_hit() {
		return get_hit;
	}

	public void setGet_hit(long get_hit) {
		this.get_hit = get_hit;
	}

	public long getGet_mis() {
		return get_mis;
	}

	public void setGet_mis(long get_mis) {
		this.get_mis = get_mis;
	}

	public long getCmd_set() {
		return cmd_set;
	}

	public void setCmd_set(long cmd_set) {
		this.cmd_set = cmd_set;
	}
	
	
	
	
	
	
	
	
	
	/*
	 { "_id" : { "$oid" : "4fddafc5dc7ec8952b513c60"} ,
 "delete_hits" : 0 ,
 "datetime" : "2012-06-17 18:21:57" ,
 "bytes" : 7550518888 ,
 "total_items" : 3713613238 ,
 "rusage_system" : "89612.447837" ,
 "listen_disabled_num" : 0 ,
 "auth_errors" : 0 ,
 "evictions" : 0 ,
 "version" : "1.4.6" ,
 "pointer_size" : 64 ,
 "time" : 1339928511 ,
 "incr_hits" : 0 ,
 "threads" : 4 ,
 "limit_maxbytes" : 8589934592 ,
 "bytes_read" : 190114223424 ,
 "curr_connections" : 187 ,
 "get_misses" : 51575653 ,
 "reclaimed" : 4244639 ,
 "bytes_written" : 37822695377 ,
 "connection_structures" : 1285 ,
 "cas_hits" : 0 ,
 "delete_misses" : 0 ,
 "total_connections" : 159136 ,
 "rusage_user" : "52799.243294" ,
 "cmd_flush" : 0 ,
 "libevent" : "1.4.4-stable" ,
 "uptime" : 3976742 ,
 "pid" : 7920 ,
 "cas_badval" : 0 ,
 "get_hits" : 85412732 ,
 "curr_items" : 84603797 ,
 "cas_misses" : 0 ,
 "accepting_conns" : 1 ,
 "cmd_get" : 136988385 ,
 "cmd_set" : 3713613243 ,
 "auth_cmds" : 0 ,
 "incr_misses" : 0 ,
 "decr_misses" : 0 ,
 "decr_hits" : 0 ,
 "conn_yields" : 0}
	 * */
	
	
	

	public long getStartTimeSecond() {
		return this.currTimeSecond - this.uptimeSecond;
	}
	
	@Override
	public String toString() {
		return "MemStatSummary [version=" + version + ", uptimeSecond=" + uptimeSecond + ", currTimeSecond="
				+ currTimeSecond + ", cmd_get=" + cmd_get + ", get_hit=" + get_hit + ", get_mis=" + get_mis
				+ ", cmd_set=" + cmd_set + ", currItems=" + currItems + ", totalItems=" + totalItems + ", usedBytes="
				+ usedBytes + ", totalBytes=" + totalBytes + ", currConns=" + currConns + ", totalConns=" + totalConns
				+ ", readBytes=" + readBytes + ", writeBytes=" + writeBytes + "]";
	}
	
	
	public long getCurrTimeSecond() {
		return currTimeSecond;
	}

	public void setCurrTimeSecond(long currTimeSecond) {
		this.currTimeSecond = currTimeSecond;
	}

}
