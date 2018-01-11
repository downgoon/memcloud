package io.memcloud.memdns.business;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MemDNSPublisherDemo {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IMemDNSPublisher memDNSPublisher = (IMemDNSPublisher)context.getBean("memDNSPublisher");
		long appId = 2;
		long userId = 0;
//		AppConfAudit audit = memDNSPublisher.publishAppConf(appId, userId);
//		System.out.println(audit);
	}

}
