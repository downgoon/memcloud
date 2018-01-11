package io.memcloud.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemCloudClient implements IMemCloudClient {

	private static final Logger log = LoggerFactory.getLogger(MemCloudClient.class);
	
	private String appid;

	private XMemcachedClient memcachedClient;

	private IMemDNSRefresh memDNSRefresh;

	private volatile boolean enableDNSRefresh = true;


	private volatile MemDNSLookup lastestDNS = null; 
	
	private CountDownLatch memDNSInitLatch = new CountDownLatch(1);
	
	private volatile Thread memDNSRefreshThread = null;
	
	private Object mutex4Start = new Object();
	
	
	@Override
	public void start() {
		if (memDNSRefreshThread != null) {
			return ;
		}
		
		if (appid == null) {
			throw new IllegalStateException("appid required for "+MemCloudClient.class.getSimpleName());
		}
		if (memcachedClient == null) {
			throw new IllegalStateException("memcachedClient required for "+MemCloudClient.class.getSimpleName());
		}
		if (memDNSRefresh == null) {
			throw new IllegalStateException("memDNSRefresh required for "+MemCloudClient.class.getSimpleName());
		}
		
		synchronized (mutex4Start) { // double-checked locking on memDNSRefreshThread instance
			if (memDNSRefreshThread==null) {
				memDNSRefreshThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						final IMemDNSRefresh refresher = memDNSRefresh; 
						while (enableDNSRefresh) {
							try {
								log.debug("mem dns refresh is lookuping ...");
								MemDNSLookup curDNS = null;
								try {
									curDNS = refresher.refreshDNS(appid);
								} catch (Exception e1) {
									log.error("men dns refresh exception detected: {}",e1.getMessage(), e1);
								}
								if ( curDNS == null ) {
									log.warn("mem dns refresh retun NULL for {}", appid);
								}
								if (curDNS != null) {
									List<MemShard> gr = curDNS.diffRemoved(lastestDNS); // group removed = {lastestDNS} - {curDNS}
									if (gr!=null && gr.size()>0) {
										String slr = MemShard.group2conf(gr);
										memcachedClient.removeServer(slr.replaceAll(",", " ") ); // XMemcached.removeServer 不支持主从格式，只支持列表格式
										log.info("remove servers ok {}",slr);
									}
									
									List<MemShard> ga = curDNS.diffAppended(lastestDNS); // group appended = {curDNS} - {lastestDNS}
									if (ga!=null && ga.size()>0) {
										String sla = MemShard.group2conf(ga);
										try {
											memcachedClient.addServer(sla); // 失败会重连的，不用考虑中间状态（某些成功加上，某些尚未加上）
											log.info("append servers ok {}",sla);
										} catch (IOException e) {
											log.warn("append servers {} exception",sla, e);
										}
									}
									
									if (lastestDNS==null) {
										lastestDNS = curDNS;
										memDNSInitLatch.countDown();
										log.info("MemDNSLookup Init OK for {}", appid);
										
									} else {
										lastestDNS = curDNS;
									}
									
								} // end for if(curDNS != null)
								
								try {
									int sec = ((lastestDNS!=null && lastestDNS.getTTLSecond() > 0) ? lastestDNS.getTTLSecond() : 3 );
									log.debug("mem dns refresh sleeping for %d sec ...", sec);
									Thread.sleep(1000L * sec);
								} catch (InterruptedException e) {
									log.warn("mem dns refresh thread interrupted while sleeping", e);
								}
								
							} catch (RuntimeException re) {//in case of exiting the loop
								log.error("unknown exception dangerous to MemDNSLookup Loop happened in MemDNSRefreshThread", re);
							}
							
							
						} //end of while (enableDNSRefresh)
					}
				}, "MemDNSRefresh-"+appid); //定时检测线程名定义为MemDNSRefresh
				
			} //end of if (memDNSRefreshThread==null)
			
		} //end of synchronized (mutex4Start)
		
		memDNSRefreshThread.start();
	}
	
	@Override
	public void stop() {
		try {
			memcachedClient.shutdown();
		} catch (IOException e) {
			log.warn(e.getMessage(),e);
			
		} finally {
			enableDNSRefresh = false;
		}
	}
	
	@Override
	public boolean isEnableDNSRefresh() {
		return enableDNSRefresh;
	}

	/**
	 * 退出MemDNS循环刷新
	 * */
	public void disableDNSRefresh() {
		this.enableDNSRefresh = false;
	}
	
	public IMemDNSRefresh getMemDNSRefresh() {
		return memDNSRefresh;
	}

	/**
	 * 如果 XMemcachedClient 初始连接没有建立完成，则阻塞等待；否则，返回XMemcachedClient客户端以操作Memcached服务器。
	 * */
	@Override
	public XMemcachedClient getMemcachedClient() {
		if (lastestDNS == null) {//MemDNSLookup Init Not Yet OK
			log.warn("Await: MemDNSLookup Init Not Yet OK for {}",appid);
			try {
				memDNSInitLatch.await();//wait for MemDNSLookup Init OK
				log.warn("Await OK: MemDNSLookup Init OK for {}",appid);
			} catch (InterruptedException e) {
				log.error("getMemcachedClient InterruptedException",e);
			}
		}
		return memcachedClient;
	}

	@Override
	public String getAppid() {
		return appid;
	}
	
	private MemCloudClient(String appid,IMemDNSRefresh memDNSRefresh,XMemcachedClient memcachedClient) {
		this.appid = appid;
		this.memDNSRefresh = memDNSRefresh;
		this.memcachedClient = memcachedClient;
	}
	
	public static MemCloudClient build(String appid,IMemDNSRefresh memDNSRefresh,XMemcachedClient memcachedClient) {
		MemCloudClient mcc = new MemCloudClient(appid,memDNSRefresh,memcachedClient);
		mcc.start();
		return mcc;
	}
	
	public static MemCloudClient buildDefault(String appid) {
		return buildDefault(appid, "");//cur pwd
	}
	
	/**
	 * @since 2012-09-14
	 * */
	public static MemCloudClient buildDefault(String appid,final int connectionPoolSize, final int opTimeoutSec) {
		return buildDefault(appid, "",connectionPoolSize,opTimeoutSec);//cur pwd
	}
	
	public static MemCloudClient buildDefault(String appid, String memDNSFilePath) {
		return buildDefault(appid, memDNSFilePath, 1, 1);
	}
	
	/**
	 * @since 2012-09-14
	 * */
	public static MemCloudClient buildDefault(String appid, String memDNSFilePath,final int connectionPoolSize, final int opTimeoutSec) {
		if (connectionPoolSize < 1 || opTimeoutSec <1) {
			throw new IllegalArgumentException("Bad connectionPoolSize or opTimeoutSec");
		}
		IMemDNSRefresh memDNSRefresh =  new MemDNSRefreshRobust(new MemDNSRefresh(), memDNSFilePath);
		
		XMemcachedClient memcachedClient = null;
		MemcachedClientBuilder builder = new XMemcachedClientBuilder();
		builder.setFailureMode(true);
		
		builder.setConnectionPoolSize(connectionPoolSize);//add by weiweili on 2012-09-14

		try {
			memcachedClient = (XMemcachedClient)builder.build();
			memcachedClient.setOpTimeout(opTimeoutSec*1000L);//add by weiweili on 2012-09-14
			
		} catch (IOException e) {
			throw new IllegalStateException("MemCloudClient build exception",e);
		}
		
		return build(appid, memDNSRefresh, memcachedClient);
	}
	
}
