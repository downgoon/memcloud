package io.memcloud.client;

import io.memcloud.client.IMemDNSRefresh;
import io.memcloud.client.MemDNSLookup;
import io.memcloud.client.MemDNSRefreshRobust;

public class MemDNSRefreshRobustDemo {

	public static void main(String[] args) {
		
//		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(new MemDNSRefresh());
		
//		demoExceptionN();
//		demoNULL1();
//		demoNULLN();
//		demoFailure();
		demoUpdate();
	}
	
	public static void demoUpdate() {
		
		
		final String appid = "10001";
		MemDNSRefreshMock mocker = new MemDNSRefreshMock();
		
		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(mocker);
		
		
		mocker.mock(new int[] {0}, 1);
		MemDNSLookup lookup1 = memDNSRefresh.refreshDNS(appid);
		
		mocker.mock(new int[] {0,1}, 2);
		MemDNSLookup lookup2 = memDNSRefresh.refreshDNS(appid);
		
		System.out.println("lookup1: "+lookup1);
		System.out.println("lookup2: "+lookup2);

	}
	
	public static void demoFailure() {
		/*
		 * MemDNS服务不可用，同时本地MemDNS文件又丢失的情况
		 * 注意：运行前删除本地文件
		 * */
		final String appid = "10001";
		MemDNSRefreshMock mocker = new MemDNSRefreshMock();
		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(mocker);
		
		mocker.mockExceptionAlways(true);
		MemDNSLookup lookup = memDNSRefresh.refreshDNS(appid);
		if (lookup != null) {
			throw new RuntimeException("测试结果有问题");
		}

	}
	
	public static void demoExceptionN() {
		final String appid = "10001";
		MemDNSRefreshMock mocker = new MemDNSRefreshMock();
		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(mocker);
		
		mocker.mockExceptionAlways(true);
		memDNSRefresh.refreshDNS(appid);
	}
	
	public static void demoNULLN() {
		final String appid = "10001";
		MemDNSRefreshMock mocker = new MemDNSRefreshMock();
		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(mocker);
		
		mocker.mockNULLAlways(true);
		memDNSRefresh.refreshDNS(appid);
	}
	
	public static void demoException1() {
		final String appid = "10001";
		MemDNSRefreshMock mocker = new MemDNSRefreshMock();
		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(mocker);
		
		mocker.mockExceptionOneTime(true);
		memDNSRefresh.refreshDNS(appid);
	}
	
	public static void demoNULL1() {
		final String appid = "10001";
		MemDNSRefreshMock mocker = new MemDNSRefreshMock();
		IMemDNSRefresh memDNSRefresh = new MemDNSRefreshRobust(mocker);
		
		mocker.mockNULLOneTime(true);
		memDNSRefresh.refreshDNS(appid);
	}
	
	public static void demoMemDNSFile() {
		demoMemDNSFileSave();
		demoMemDNSFileRead();
	}
	
	public static void demoMemDNSFileSave() {
		final String appid = "11011";
		MemDNSRefreshMock mock = new MemDNSRefreshMock();
		MemDNSLookup lookup = mock.refreshDNS(appid);
		
		MemDNSRefreshRobust.MemDNSRefreshFile dnsFile = new MemDNSRefreshRobust.MemDNSRefreshFile("memcloud.dns.");
		dnsFile.saveDNS(appid, lookup);
		
	}
	
	public static void demoMemDNSFileRead() {
		final String appid = "11011";
		MemDNSRefreshRobust.MemDNSRefreshFile dnsFile = new MemDNSRefreshRobust.MemDNSRefreshFile("memcloud.dns.");
		MemDNSLookup lookup = dnsFile.refreshDNS(appid);
		System.out.println(lookup);
		System.out.println(lookup.getTimestamp());
	}
	

}
