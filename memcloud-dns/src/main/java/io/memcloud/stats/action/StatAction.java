package io.memcloud.stats.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.github.downgoon.jresty.commons.utils.DateUtil;
import com.github.downgoon.jresty.rest.view.DefaultHttpHeaders;
import io.memcloud.driver.mongodb.IMongodbStatManager;
import io.memcloud.driver.mongodb.StatDBObject;
import io.memcloud.memdns.action.BaseAction;
import io.memcloud.memdns.util.CsvMedia;
import io.memcloud.stats.dao.IMemStatDao;
import io.memcloud.stats.model.MemStatSummary;

public class StatAction extends BaseAction  {

	private static final long serialVersionUID = 3712165401650005372L;
	
	@Resource(name = "mongodbStatManager")
	protected IMongodbStatManager mongodbStatManager;
	
	@Resource(name = "memStatDao")
	private IMemStatDao memStatDao;
	

	@Override
	public String view() {
		Object[] triple = parseParam();
		if (triple==null || triple.length != 3) {
			responseModel.setStatus(401);
			responseModel.setMessage("ip,port,d required");
			return REST("view");
		}
		String ip = (String)triple[0];
		int port = (Integer)triple[1];
		Date curDate = (Date)triple[2];
		// Date preDate = DateUtil.someDaysAgo(curDate, 1);
		
		Date preDate = DateUtil.someDaysAgo(curDate, 1);
		
		Map<String,Object> attachment = new LinkedHashMap<String, Object>();
		responseModel.setAttachment(attachment);
		// String curDateTxt = DateUtil.format(curDate, "yyyyMMdd");
		String curDateTxt = DateUtil.format(curDate, "yyyyMMdd");
		attachment.put("ip", ip);
		attachment.put("port", port);
		attachment.put("curDate", curDateTxt);
		// attachment.put("preDate", DateUtil.format(preDate, "yyyyMMdd"));
		attachment.put("preDate", DateUtil.format(preDate, "yyyyMMdd"));
		MemStatSummary summary = memStatDao.summary(ip, port);
		if (summary == null) {
			responseModel.setStatus(402);
			responseModel.setMessage("meminstance not found "+ip+":"+port);
			return REST("view");
		}
		attachment.put("summary", summary);
		String[] cmds = new String[] {"get","set","hit","mis"};
		for (String cmd : cmds) {
			attachment.put("trend-"+cmd, "memcloud/stat-"+cmd+".html?triple="+ip+"_"+port+"_"+curDateTxt);
		}
		
		return REST("view");
	}

	/**
	 * 近两天（今天与昨天）各个时段（5分钟为单位描点）访问量（读写，读，写）对比曲线图
	 * */
	public String trend() {
		if (paramId == null) {
			paramId = "123456";
		}
		/* 
		 * 输出的数据格式： {"Table":["DateTime","VideoValue","MediaValue","UserValue"], "Data":[[20111101,1000,2000,3000],[20111102,2000,5000,4000], [20111103,3000,6000,1000],[20111104,4000,1000,3400]],"Title":["指数","搜索","播放"], "Name":"还珠格格","Id":1}
		 * 曲线图一次性加载所有的（每一天的），按周，按月，按季，按年显示让前端Flash展现。 
		 * */
//		List<AlbumStatDaily> list = albumStatDailyManager.indexCurveGraph(dateA, paramId, timeSpanType);
		responseModel.setStatus(RC_SUCC);
		responseModel.setMessage("");
		
		Random r = new Random();
		Map<String,Object> attachment = new LinkedHashMap<String, Object>();
		attachment.put("Name", "对象"+paramId);
		attachment.put("Id", paramId);
//		attachment.put("Title", Arrays.asList("指数","搜索","播放"));
//		attachment.put("Table", Arrays.asList("DateTime","VideoValue","MediaValue","UserValue"));
		attachment.put("Title", Arrays.asList("VI(指数曲线)","SI(搜索曲线)","PI(播放曲线)"));
		attachment.put("Table", Arrays.asList("DateTime","VI","SI","PI"));//DateTime 名字不能随便换，必须固定
		List<List<Object>> data = new ArrayList<List<Object>>();
		attachment.put("Data", data);
		for (int i=1; i<30;i++) {
			List<Object> item = new ArrayList<Object>();
//			item.add(DateUtil.format(asd.getStatDate(),"yyyyMMdd"));//日期
//			item.add("201206"+String.format("%02d", i));//日期
			item.add(i);//日期  （日期不是日期格式的虽然能够表现曲线图，但是时间轴不美观）
//			item.add("-");//指数
			item.add(r.nextInt(100));//指数
			item.add(r.nextInt(100));//搜索
//			item.add("-");//播放
			item.add(r.nextInt(100));//播放
			data.add(item);
		}
		responseModel.setAttachment(attachment);
		return REST("csv");
	}
	
	/**
	 * memcached实例当前状态
	 * @return
	 */
	public String curr(){
		StatDBObject object = null;
//		object = mongodbStatManager.getCurrentStat(getCollName());
		Map<String,Object> attachment = new LinkedHashMap<String, Object>();
		attachment.put("Data", object);
		
		responseModel.setStatus(RC_SUCC);
		responseModel.setMessage("ok");
		responseModel.setAttachment(attachment);	
		return REST(new DefaultHttpHeaders(SUCCESS).withStatus(200).addHeader("PSN", "1/1"), false);
	}
	
	/**
	 * get请求统计
	 * @return CSV格式数据
	 */
	public String get() {
		return cmd(0,"GET");
	}
	
	/**
	 * set请求统计
	 * @return
	 */
	public String set(){
		return cmd(1,"SET");
	}
	
	/**
	 * hit统计
	 * @return
	 */
	public String hit(){
		return cmd(2,"Hit");
	}
	
	/**
	 * miss统计
	 * @return
	 */
	public String mis(){
		return cmd(3,"Mis");
	}
	
	/**
	 * {0:GET,1:SET,2:Hit,3:Mis}
	 * */
	private String cmd(int cmdType, String cmdName) {
		Object[] triple = parseParam();
		if (triple==null || triple.length != 3) {
			responseModel.setStatus(401);
			responseModel.setMessage("ip,port,d required");
			return REST("nohtml");
		}
		String ip = (String)triple[0];
		int port = (Integer)triple[1];
		Date curDate = (Date)triple[2];
		Date preDate = DateUtil.someDaysAgo(curDate, 1);
		
		StringBuffer attachment = new StringBuffer();
		responseModel.setAttachment(attachment);
		attachment.append(cmdName+" Command Curve for MemInstance ").append(ip).append(":").append(port).append("\r\n");
		//时间段；昨天量；今天量
		attachment.append("Time;").append(DateUtil.format(preDate,"yyyy-MM-dd")).append(";").append(DateUtil.format(curDate,"yyyy-MM-dd")).append("\r\n");
		
		LinkedHashMap<String, Long> fPre = memStatDao.trendCmd(ip, port, preDate,cmdType);
		LinkedHashMap<String, Long> fCur = memStatDao.trendCmd(ip, port, curDate,cmdType);
		
//		if (fPre != null && fPre.size() > 0) {//平时，以昨天为参考基准
//			Iterator<Entry<String, Long>> iterBase = fPre.entrySet().iterator();
//			while(iterBase.hasNext()) {
//				Entry<String, Long> eBase = iterBase.next();
//				attachment.append(eBase.getKey()).append(";");//csv:X
//				attachment.append(eBase.getValue()).append(";");//csv:Y1
//				attachment.append(fCur.get(eBase.getKey()));//csv:Y2 （表现层Flash支持数据NULL，表示数据断点）
//				attachment.append("\r\n");//csv:NextLine
//			}
//		} else {//第一天上线，只有当天的数据
//			Iterator<Entry<String, Long>> iterBase = fCur.entrySet().iterator();
//			while(iterBase.hasNext()) {
//				Entry<String, Long> eBase = iterBase.next();
//				attachment.append(eBase.getKey()).append(";");//csv:X
//				attachment.append(fPre.get(eBase.getKey())).append(";");//csv:Y2 （表现层Flash支持数据NULL，表示数据断点）
//				attachment.append(eBase.getValue());//csv:Y1
//				attachment.append("\r\n");//csv:NextLine
//			}
//		}
		
		CsvMedia.merge(fPre, fCur, attachment);
		return CsvMedia.csv(getHttpResponse(), attachment);
	}

	/**
	 * 接口支持三种格式参数：
	 * 1、ip=10.10.79.214&port=11211&d=20120617
	 * 2、triple=10.10.79.214_11211_20120617
	 * 3、\10.10.79.214:11211.html?d=20120617
	 * @param ip IP地址
	 * @param port 端口
	 * @param d 统计日期，可选。默认昨天。格式：yyyyMMdd
	 * @return	new Object[] {IP:String, Port:Integer, curDate:Date};
	 * */
	protected Object[] parseParam() {
		Object[] triple = new Object[3];
		
		String d = getParam("d");//date
		if (StringUtils.isNotEmpty(getParam("triple"))) {
			String[] t = getParam("triple").trim().split("_");
			if (t==null || t.length <3) {
				return null;
			}
			paramId = t[0]+":"+t[1];
			d = t[2];
		}
		
		if (StringUtils.isEmpty(paramId)) {
			if (StringUtils.isEmpty(getParam("ip")) || StringUtils.isEmpty(getParam("port"))) {
				return null;
			}
			paramId = getParam("ip")+":"+getParam("port");
		} 
		paramId = paramId.trim();
		if (! paramId.trim().matches("(\\d{1,3}\\.){3}\\d{1,3}:\\d+")) {
			return null;//'ip:port' required, eg. 10.10.79.214:11211
		} 
		int idx = paramId.indexOf(":");
		String ip = paramId.substring(0, idx);
		Integer port = null;
		try {
			port = Integer.parseInt(paramId.substring(idx+1));
		} catch (NumberFormatException e1) {//port超出范围
			return null;
		}
		//参数1： IP
		triple[0] = ip;
		//参数2： 端口
		triple[1] = port;
		
		//参数3：统计日期
		Date curDate = null;
		if (StringUtils.isEmpty(d)) {
			curDate = DateUtil.todayBegin();
		} else {
			if (d.length() != 8) {//"'d' format 'yyyyMMdd' required, eg. 20120614"
				return null;
			} else {
				curDate = DateUtil.parse(d,"yyyyMMdd");
			}
		}
		triple[2] = curDate;
		return triple;
	}
	
	private String paramId;
	
	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

}
