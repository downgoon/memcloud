package io.memcloud.memdns.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.IUserDao;
import io.memcloud.memdns.dao.entry.User;

public class UserDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext-dao.xml" });
		IUserDao userDao = (IUserDao) applicationContext.getBean("userDao");
		
		User user = new User();
		user.setName("weiweili");
		
		user.setPwd("123456");
//		user.setEmail("");
		user.setMobile("13810855235");
		userDao.save(user);
		
		User u = userDao.getByName("weiweili");
		System.out.println(u);
	}

}
