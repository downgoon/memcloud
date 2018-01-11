package io.memcloud.stats.business;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestMemInstanceFaultManager {

	private IMemInstanceFaultManager memInstanceFaultManager;
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		memInstanceFaultManager = (IMemInstanceFaultManager)factory.getBean("memInstanceFaultManager");
		
	}
	
	@Test
	public void test(){
		for(int i=0;i<6;i++){
			memInstanceFaultManager.sendFaultMessage("10.10.83.180", 18602); 
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<28;i++){
			memInstanceFaultManager.sendFaultMessage("10.10.83.180", 18602); 
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
