package io.memcloud.memdns.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.memcloud.memdns.dao.ConcurrencyProblemException;
import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.IMemHostDao;
import io.memcloud.memdns.dao.IMemInstanceDao;
import io.memcloud.memdns.dao.IScaleoutAppealDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.memdns.dao.entry.MemHost;
import io.memcloud.memdns.dao.entry.MemInstance;
import io.memcloud.memdns.dao.entry.MemInstancePeer;
import io.memcloud.memdns.dao.entry.ScaleoutAppeal;

public class MemInstanceDirector implements IMemInstanceDirector {

	protected static final Logger log = LoggerFactory.getLogger(MemInstanceDirector.class);
	
	@Resource(name = "memHostDao")
	private IMemHostDao memHostDao;
	
	@Resource(name = "memInstanceDao")
	private IMemInstanceDao memInstanceDao;
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "scaleoutAppealDao")
	private IScaleoutAppealDao scaleoutAppealDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	@Override
	public List<AppMemGroup> allocate(AppDesc appDesc, int shardAmount,int memSizeM, ScaleoutAppeal appeal)
	throws ConcurrencyProblemException {
		if (appeal==null || appeal.getStatus()!=ScaleoutAppeal.STATUS_WAITING) {
			throw new IllegalArgumentException("ScaleoutAppeal not STATUS_WAITING for allocating");
		}
		//保证申请记录的更新和资源的分配在同一个事务里面
		List<AppMemGroup> group = allocate(appDesc, shardAmount, memSizeM);
		if ( group!=null ) {
			
			Short status = appeal.getStatus();
			Long passedTime = appeal.getPassedTime();
			Integer passedShard = appeal.getPassedShard();
			Integer passedMem = appeal.getPassedMem();
					
			try {
				appeal.setStatus(ScaleoutAppeal.STATUS_PASSED);//审核通过
				appeal.setPassedTime(System.currentTimeMillis());
				appeal.setPassedShard(shardAmount);
				appeal.setPassedMem(memSizeM);
				
				scaleoutAppealDao.update(appeal);
			} catch (RuntimeException re) {//TODO 数据库回滚的时候，Bean的状态能回滚吗？
				
				appeal.setStatus(status);
				appeal.setPassedTime(passedTime);
				appeal.setPassedShard(passedShard);
				appeal.setPassedMem(passedMem);
				throw re;
			}
		}
		return group;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	@Override
	public List<AppMemGroup> allocate(AppDesc appDesc, int shardAmount, int memSizeM) 
	throws ConcurrencyProblemException 
	{
		//竞争资源的分配，需要考虑并发问题。是否考虑在存储过程中完成分配与更新操作？
		//Read Unused Resources
		List<MemInstancePeer> peers = memInstanceDao.getUnusedPeer(0,shardAmount,memSizeM);
		if (peers==null || peers.size()<=0 || peers.size() < shardAmount) {//资源池资源耗尽（资源不足的时，干脆不分配）
			return null;
		}
		
		List<Long> memIds = new ArrayList<Long>(peers.size()*2);
		for (MemInstancePeer p : peers) {
			memIds.add(p.getMid());
			memIds.add(p.getSid());
		}
		//Update Resources (setting used flag)
		int count = memInstanceDao.allocUnusedPeer(memIds);
		if (count < memIds.size()) {//Lost Update Problem Detected, Rollback the transaction
			throw new ConcurrencyProblemException("Lost Update Problem Detected in meminstance allocated ("+count+","+memIds.size()+")");
		} 
		
		if (appDesc.getStatus() == AppDesc.STATUS_INITIALIZE) {//第一次分配资源
			appDesc.setStatus(AppDesc.STATUS_ALLOCATED);
			appDescDao.update(appDesc);
		}
		
		List<AppMemGroup> list = new ArrayList<AppMemGroup>();
		for (MemInstancePeer p : peers) {
			AppMemGroup g = new AppMemGroup();
			g.setAppId(appDesc.getId());
			g.setMasterIP(p.getMhost());
			g.setMasterPort(p.getMport());
			g.setSlaveIP(p.getShost());
			g.setSlavePort(p.getSport());
			g.setStatus(0);
			list.add(g);
		}
		appMemGroupDao.save(list);
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
	@Override
	public MemInstance bringin(String memCmd) throws IllegalArgumentException {
		Map<String,Object> args = null;
		try {
			args = parseMemCmd(memCmd);
		} catch (ParseException e) {
			new IllegalArgumentException(e.getMessage(), e);
		}
		//localIP+localPort 唯一索引； localIP+repcPort 唯一索引
		String localIP = (String)args.get("l");
		Integer localPort = (Integer)args.get("p");
		Integer repcPort = (Integer)args.get("X");
		
		String peerIP = (String)args.get("x");
		Integer argMem = (Integer)args.get("m");
		Integer argConn = (Integer)args.get("c");
		
		
		
		MemHost memHost = memHostDao.getByIP(localIP); 
		if (memHost == null) {
			memHost = new MemHost();
			memHost.setIp(localIP);
			memHostDao.save(memHost);
		}
		
		MemInstance memLocal = memInstanceDao.get(localIP, localPort);
		if (memLocal == null) {
			memLocal = new MemInstance();
			memLocal.setHostid(memHost.getId());
			
			memLocal.setHostIp(localIP);
			memLocal.setPort(localPort);
			memLocal.setPeerIp(peerIP);
			memLocal.setRepcPort(repcPort);
			memLocal.setArgMem(argMem);
			memLocal.setArgConn(argConn);
			
			memLocal.setMemCmd(memCmd);
			memLocal.setStatus(0);//刚入库，尚未分配
			
			//Peer Intalled 字段是否需要 ?
			MemInstance memPeer = memInstanceDao.getByRepc(peerIP, repcPort);
			if (memPeer != null) {//本次登记的是孪生兄弟中的弟弟
				memLocal.setRoleInPeer(MemInstance.YOUNG_IN_PEER);
				if (! memPeer.isPeerInstalled()) {//为哥哥补充弟弟的信息（因为早在哥哥登记时，弟弟还没出生）
					memPeer.setRoleInPeer(MemInstance.ELDER_IN_PEER);
					memInstanceDao.update(memPeer);
				}
			} else {//本次登记的是孪生兄弟中的哥哥（等弟弟出生时，会修改哥哥的这个标记），但当前依然没有兄弟
				memLocal.setRoleInPeer(MemInstance.NONE_IN_PEER);
			}
			memInstanceDao.save(memLocal);
		}
		
		return memLocal;
	}
	
	private static String[] argNames = new String[] {
		"l","p","x","X","m","c"
	}; 
	
	private static int[] argTypes = new int[] {//0:String, 1:Integer, 2:Long
		0,1,0,1,1,1
	}; 
	private  static String[] argDescs = new String[] {
		"arg l required for local ip",
		"arg p required for local port",
		"arg x required for peer ip",
		"arg x required for repc port",
		"arg m required for memory size",
		"arg c reqired for conn limit"
	};
	private static Options opts = new Options();
	static {
		int i = 0;
		while (i < argNames.length) {
			opts.addOption(argNames[i], true, argDescs[i]);
			i ++;
		}
		//other options 可拓展性太差
		opts.addOption("d", false, "daemon option");
		opts.addOption("u", false, "user option");
		opts.addOption("P", false, "pid file");
	}
	
	public static Map<String,Object> parseMemCmd(String memCmd) throws ParseException {
		return parseMemCmd0(memCmd);
	}
	
	/** 警告：本实现可拓展性很差，memcached 指令中不能拓展没有预知的参数 */
	static Map<String,Object> parseMemCmd0(String memCmd) throws ParseException {
		String[] args = splitArgs(memCmd);
		if (log.isDebugEnabled()) {
			log.debug("args is %s",Arrays.asList(args));
		}
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		
		BasicParser parser = new BasicParser();
		CommandLine cli = parser.parse(opts, args,false);;
		StringBuffer err = new StringBuffer();
		int i = 0;
		while (i < argNames.length) {
			if (! cli.hasOption(argNames[i])) {
				err.append(argDescs[i]).append(";");
			} else {
				Object v = cli.getOptionValue(argNames[i]);//String
				switch (argTypes[i]) {
				case 1:
					v = Integer.parseInt((String)v);
					break;
				case 2:
					v = Long.parseLong((String)v);
				default:
					break;
				}
				map.put(argNames[i], v);
			}
			i ++;
		}
		
		return map;
	}
	

	private static String[] splitArgs(String cmd) {
		StringTokenizer st = new StringTokenizer(cmd);
		String[] args = new String[st.countTokens()];
	    int c = 0;
		while(st.hasMoreElements() ){
			args[c] = st.nextToken();
			c++;
	    }
		return args;
	} 
}
