package io.memcloud.memdns.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import io.memcloud.memdns.business.IMemInstanceDirector;
import io.memcloud.memdns.dao.IMemInstanceDao;
import io.memcloud.memdns.dao.entry.MemInstance;
import io.memcloud.memdns.dao.entry.MemInstancePeer;

import com.github.downgoon.jresty.rest.view.DefaultHttpHeaders;

/** {@link MemInstance} 资源池中的资源引进和分配 */
public class MemAction extends BaseAction {

	private static final long serialVersionUID = 8368890181731718451L;
	
	@Resource(name = "memInstanceDirector")
	private IMemInstanceDirector memInstanceDirector;
	
	@Resource(name = "memInstanceDao")
	private IMemInstanceDao memInstanceDao;
	
	@Override
	public String create() {
		//DEMO: /usr/local/bin/memcached -d -p 18604 -m 1024 -x 10.10.83.180 -X 18634 -u root -l 10.10.83.95 -c 256 -P /tmp/memcloud_idxdeps_18604_10.10.83.180_18634.pid
		String cmd = getParam("cmd");
		if (StringUtils.isEmpty(cmd)) {
			responseModel.setStatus(401);
			responseModel.setMessage("cmd required");
			return REST(new DefaultHttpHeaders("nohtml").withStatus(401));
		}
		MemInstance memInstance = memInstanceDirector.bringin(cmd);
		if (memInstance == null) {
			responseModel.setStatus(402);
			responseModel.setMessage("fail");
			responseModel.setDebug(cmd);
			return REST(new DefaultHttpHeaders("nohtml").withStatus(402));
		}
		responseModel.setAttachment(memInstance);
		responseModel.setDebug(cmd);
		return REST(new DefaultHttpHeaders("nohtml").withStatus(200));
	}
	
	/**
	 * 资源池中剩余的资源
	 * */
	@Override
	public String index() { 
		if (mem == null || mem < 100) {
			mem = 100;
		} 
		List<MemInstancePeer> freePeer = memInstanceDao.getUnusedPeer(0, 20, mem);//>=mem的空闲资源
		responseModel.setAttachment(freePeer);
		return REST("index");
	}
	
	private Integer mem;

	public void setMem(Integer mem) {
		this.mem = mem;
	}
	
	
	
}
