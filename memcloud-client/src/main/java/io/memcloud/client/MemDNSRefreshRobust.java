package io.memcloud.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 	第一次（初始化）MemDNS查询时，如果MemDNS服务器失效，则从本地文件中加载之前的查询结果。
 * 	如果MemDNS的版本有更新，则需把最新查询结果保存到本地文件。
 * */
public class MemDNSRefreshRobust implements IMemDNSRefresh {

	private static Logger log = LoggerFactory.getLogger(MemDNSRefreshRobust.class);
	
	private IMemDNSRefresh delegate;
	
	private MemDNSRefreshFile memDNSFile;
	
	private volatile MemDNSLookup dnsFileCache = null;
	
	private volatile boolean initOK = false;
	
	private int initTryMaxTimes = 3;
	
	public MemDNSRefreshRobust(IMemDNSRefresh delegate) {
		this(delegate, null);
	}
	
	public MemDNSRefreshRobust(IMemDNSRefresh delegate,String memDNSFilePath) {
		this.delegate = delegate;
		String path = memDNSFilePath;
		if (memDNSFilePath==null || "".equalsIgnoreCase(memDNSFilePath.trim())) {
			path = "";
		}
		this.memDNSFile = new MemDNSRefreshFile(path+"memcloud.dns.");
	}

	/**
	 * 非线程安全
	 * */
	@Override
	public MemDNSLookup refreshDNS(String appid) {
		
		MemDNSLookup dnsFly = null;
		/*
		 * 初始化阶段如果失败，则连续尝试N次；其他时间失败，不需重试。
		 * */
		int initTry = 0;
		do {
			try {
				dnsFly = delegate.refreshDNS(appid);
			} catch (RuntimeException re) {
				log.warn("mem dns refresh exception in MemDNSRefreshRobust.delegete", re);
				
			} finally {
				initTry ++;
			}
			if ( !initOK && dnsFly!=null ) {
				initOK = true;
			}
			
		} while(!initOK && initTry < initTryMaxTimes);
		
		
		MemDNSLookup r = null;
		if (initOK) {//日常调用（非第一次调用），则无论dnsFly是否为空，都直接返回。
			r = dnsFly;
			
		} else if (dnsFly==null) {//第一次调用，并且遇到MemDNS服务失效，则从文件加载MemDNS历史查询结果
			MemDNSLookup dnsFile = memDNSFile.refreshDNS(appid);
			if (dnsFile != null) {
				dnsFileCache = dnsFile;
				log.info("mem dns init from file for {} OK: {}", appid, dnsFile);
			} else {
				log.warn("mem dns init from file failure for {}",appid);
			}
			initOK = true;//本地文件也可能加载为NULL，但依然初始化完成
			r = dnsFile;
		}
		
		/* 实时查询结果与本地文件结果版本比对 （无论第一次调用，还是日常调用） */
		if (dnsFly!= null && (dnsFileCache==null || dnsFly.getVersion().intValue() != dnsFileCache.getVersion()) ) {
			dnsFileCache = dnsFly;
			memDNSFile.saveDNS(appid, dnsFly);
		}
		return r;
	}
	
	/** 从本地文件加载MemDNS结果 */
	static class MemDNSRefreshFile implements IMemDNSRefresh {
		/*
		 * 文件持久化的格式是“逐行文件”，文件名以appid为后缀，内容定义如下：
		 * 第1行，必选，存放：MemDNSLookup.version
		 * 第2行，必选，存放：MemDNSLookup.memGroup 的conf格式
		 * 第3行，可选，存放：MemDNSLookup.TTLSecond
		 * 第4行，可选，存放：MemDNSLookup.timestamp
		 * */

		private String filePrefix;
		
		public MemDNSRefreshFile(String filePrefix) {
			super();
			this.filePrefix = filePrefix;
		}

		@Override
		public MemDNSLookup refreshDNS(String appid) {
			final String fileName = filePrefix+appid;
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			} catch (FileNotFoundException e) {
				log.warn("memcloud init mem dns file not found: {}",fileName, e);
				return null;
			} 
			
			try {
				MemDNSLookup lookup = new MemDNSLookup();
				try {
					String version = reader.readLine();
					try {
						lookup.setVersion(Integer.parseInt(version));
					} catch (NumberFormatException e) {
						log.warn("memcloud init mem dns file {} format error on version",fileName,e);
						return null;
					}
					
					String conf = reader.readLine();
					List<MemShard> group = MemShard.conf2group(conf);
					if (group==null) {
						log.warn("memcloud init mem dns file {} format error on group {}",fileName,conf);
						return null;
					}
					lookup.setMemGroup(group);
					
					try {
						String ttl = reader.readLine();//optional arg ttl
						if (ttl!=null) {
							lookup.setTTLSecond(Integer.parseInt(ttl));
						} else {
							lookup.setTTLSecond(3);
						}
						String tm = reader.readLine();//optional arg timestamp
						if (tm != null) {
							lookup.setTimestamp(Long.parseLong(tm));
						} else {
							lookup.setTimestamp(System.currentTimeMillis());
						}
					} catch (NumberFormatException e) {
						log.warn("memcloud init mem dns file {} format error on ttl or tm", fileName);
						lookup.setTTLSecond(3);
						lookup.setTimestamp(System.currentTimeMillis());
					}
					
				} catch (IOException e) {
					log.warn("memcloud init mem dns file io exception: {}",fileName, e);
					return null;
				}
				return lookup;
				
			} finally {
				if (reader!=null) {
					try {
						reader.close();
					} catch (IOException e) {
						log.error("file "+fileName+" close exception",e);
					}
				}
			}
		}
		

		/** 将MemDNS结果保存到文件 */
		void saveDNS(String appid,MemDNSLookup lookup) {
			final String fileName = filePrefix+appid;
			try {
				_save(fileName, lookup);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("file not found: "+fileName, e);
			}
		} 
		
		private boolean _save(String fileName,MemDNSLookup lookup) throws FileNotFoundException {
			PrintWriter writer = new PrintWriter(new File(fileName));
			try {
				writer.println(lookup.getVersion());
				
				String conf = MemShard.group2conf(lookup.getMemGroup());
				writer.println(conf);
				
				writer.println(lookup.getTTLSecond());
				writer.println(lookup.getTimestamp());
				writer.flush();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
			return true;
		}
		
	}

}
