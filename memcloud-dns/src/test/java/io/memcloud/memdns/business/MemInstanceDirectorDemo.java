package io.memcloud.memdns.business;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.memdns.dao.entry.MemInstance;

public class MemInstanceDirectorDemo {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IMemInstanceDirector director = (IMemInstanceDirector)context.getBean("memInstanceDirector");
		AppDesc appDesc = new AppDesc();
		appDesc.setId(4L);
		List<AppMemGroup> list = director.allocate(appDesc, 2, 1024);
		System.out.println(list);
		/*
Hibernate: select meminstanc0_.repc_port as col_0_0_, meminstanc0_.host_ip as col_1_0_, meminstanc0_.port as col_2_0_, meminstanc0_.id as col_3_0_, meminstanc1_.host_ip as col_4_0_, meminstanc1_.port as col_5_0_, meminstanc1_.id as col_6_0_, meminstanc0_.arg_mem as col_7_0_ from mem_instance meminstanc0_, mem_instance meminstanc1_ where meminstanc0_.host_ip=meminstanc1_.peer_ip and meminstanc0_.repc_port=meminstanc1_.repc_port and meminstanc0_.status=? and meminstanc0_.role_in_peer=? and meminstanc0_.arg_mem>=? order by meminstanc0_.arg_mem, meminstanc0_.id limit ?
Hibernate: update mem_instance as m set m.status = 1 where m.id in ( 1,2,3,4 ) and m.status = 0
Hibernate: insert into app_mem_group (appid, master_ip, master_port, slave_ip, slave_port, status) values (?, ?, ?, ?, ?, ?)
Hibernate: select last_insert_id()
Hibernate: insert into app_mem_group (appid, master_ip, master_port, slave_ip, slave_port, status) values (?, ?, ?, ?, ?, ?)
Hibernate: select last_insert_id()
		 * */
	}
	
	static void bringin() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IMemInstanceDirector memInstanceDirector = (IMemInstanceDirector)context.getBean("memInstanceDirector");
		MemInstance mem = memInstanceDirector.bringin("/usr/local/bin/memcached -d -p 12106 -m 1024 -x 10.10.83.177 -X 12156 -u root -l 10.10.83.178  -c 256 -P /tmp/memcloud_12106_10.10.83.177_12156.pid");
		System.out.println("RoleInPeer="+mem.getRoleInPeer());
	}
	
	static void init() throws Exception {
		String cmdElder = "/usr/local/bin/memcached -d -p 18603 -m 1024 -x 10.10.83.95 -X 18634 -u root -l 10.10.83.180 -c 256 -P /tmp/memcloud_idxdeps_18603_10.10.83.95_18634.pid";
		String cmdYoung = "/usr/local/bin/memcached -d -p 18604 -m 1024 -x 10.10.83.180 -X 18634 -u root -l 10.10.83.95 -c 256 -P /tmp/memcloud_idxdeps_18604_10.10.83.180_18634.pid";
		
		Map<String,Object> map = MemInstanceDirector.parseMemCmd(cmdElder);
		System.out.println(map);
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IMemInstanceDirector memInstanceDirector = (IMemInstanceDirector)context.getBean("memInstanceDirector");
		MemInstance memElder = memInstanceDirector.bringin(cmdElder);
		System.out.println(memElder);
		MemInstance memYoung = memInstanceDirector.bringin(cmdYoung);
		System.out.println(memYoung);
	}

}
