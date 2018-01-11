package io.memcloud.memdns.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

public class CsvMedia {
	
	public static void main(String[] args) {
		StringBuffer attachment = null;
		LinkedHashMap<String, Long> fPre = null;
		LinkedHashMap<String, Long> fCur = null;
		
		//C1： 没有统计数据
		attachment = new StringBuffer("GET Command Curve for MemInstance 10.10.83.95:18601\r\n");
		fPre = null;
		fCur = null;
		merge(fPre, fCur, attachment);
		System.out.println(attachment);
		
		//C2：没有统计数据
		attachment = new StringBuffer("GET Command Curve for MemInstance 10.10.83.95:18601\r\n");
		fPre = new LinkedHashMap<String, Long>();
		fCur = new LinkedHashMap<String, Long>();
		merge(fPre, fCur, attachment);
		System.out.println(attachment);
		
		//C3：昨天有统计数据
		attachment = new StringBuffer("GET Command Curve for MemInstance 10.10.83.95:18601\r\n");
		fPre = new LinkedHashMap<String, Long>();
		fCur = new LinkedHashMap<String, Long>();

		fPre.put("14:50", 4704L);
		fPre.put("15:00", 13743L);
		fPre.put("15:10", 9343L);
		fPre.put("15:20", 9898L);
		
		merge(fPre, fCur, attachment);
		System.out.println(attachment);
		
		//C4：今天有统计数据
		attachment = new StringBuffer("GET Command Curve for MemInstance 10.10.83.95:18601\r\n");
		fPre = new LinkedHashMap<String, Long>();
		fCur = new LinkedHashMap<String, Long>();

		fCur.put("14:50", 4704L);
		fCur.put("15:00", 13743L);
		fCur.put("15:10", 9343L);
		fCur.put("15:20", 9898L);
		
		merge(fPre, fCur, attachment);
		System.out.println(attachment);
		
		//C5：昨天下午和今天早上有统计数据
		attachment = new StringBuffer("GET Command Curve for MemInstance 10.10.83.95:18601\r\n");
		fPre = new LinkedHashMap<String, Long>();
		fCur = new LinkedHashMap<String, Long>();

		fPre.put("14:50", 4704L);
		fPre.put("15:00", 13743L);
		fPre.put("15:10", 9343L);
		fPre.put("15:20", 9898L);
		
		fCur.put("00:10", 9039L);
		fCur.put("00:20", 7014L);
		fCur.put("00:30", 6555L);
		fCur.put("00:40", 4847L);
		
		merge(fPre, fCur, attachment);
		System.out.println(attachment);
		
		//C6：昨天和今天重叠时间上都有统计数据
		attachment = new StringBuffer("GET Command Curve for MemInstance 10.10.83.95:18601\r\n");
		fPre = new LinkedHashMap<String, Long>();
		fCur = new LinkedHashMap<String, Long>();

		fPre.put("14:50", 4704L);
		fPre.put("15:00", 13743L);
		fPre.put("15:10", 9343L);
		fPre.put("15:20", 9898L);
		
		fCur.put("14:50", 9039L);
		fCur.put("15:00", 7014L);
		fCur.put("15:10", 6555L);
		fCur.put("15:20", 4847L);
		
		merge(fPre, fCur, attachment);
		System.out.println(attachment);
		
	}
	
	
	/**
	 * @param	fPre	键集必须有序，昨天各时间段的统计数据
	 * @param	fCur	键集必须有序，今天各时间段的统计数据
	 * @param	attachment	CSV 报表
	 * */
	public static void merge(LinkedHashMap<String, Long> fPre, LinkedHashMap<String, Long> fCur, StringBuffer attachment) {
		
		Iterator<Entry<String, Long>> iterPre = null;
		if (fPre!=null && fPre.size()>0) {
			iterPre = fPre.entrySet().iterator();
		}
		
		Iterator<Entry<String, Long>> iterCur = null;
		if (fCur!=null && fCur.size()>0) {
			iterCur = fCur.entrySet().iterator();
		}
		
		Entry<String, Long> ePre = nextEntry(iterPre);
		Entry<String, Long> eCur = nextEntry(iterCur);
		
		while ( ePre!=null && eCur!=null ) {
			int c = ePre.getKey().compareTo(eCur.getKey());
			if (c <= -1) {
				appendItem(ePre.getKey(), ePre.getValue(), null, attachment);
				ePre = nextEntry(iterPre);
				
			} else if (c>=1) {
				appendItem(eCur.getKey(), null, eCur.getValue(), attachment);
				eCur = nextEntry(iterCur);
				
			} else {//c==0
				appendItem(eCur.getKey(), ePre.getValue(), eCur.getValue(), attachment);
				ePre = nextEntry(iterPre);
				eCur = nextEntry(iterCur);
			}
		}
		
		while (ePre!=null) {
			appendItem(ePre.getKey(), ePre.getValue(), null, attachment);
			ePre = nextEntry(iterPre);
		}
		while (eCur!=null) {
			appendItem(eCur.getKey(), null, eCur.getValue(),attachment);
			eCur = nextEntry(iterCur);
		}
		
	}
	
	private static void appendItem(String x, Long y1, Long y2, StringBuffer attachment) {
		attachment.append(x).append(";");//csv:X
		attachment.append(y1).append(";");//csv:Y1
		attachment.append(y2);//csv:Y2
		attachment.append("\r\n");//csv:NextLine
	}
	
	private static Entry<String, Long> nextEntry(Iterator<Entry<String, Long>> iter) {
		if (iter!=null && iter.hasNext()) {
			return iter.next();
		} 
		return null;
	}
	

	public static String csv(HttpServletResponse httpRespone, StringBuffer attachment) {
		byte[] httpBody = null;
		try {
			httpBody = attachment.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpRespone.setContentLength(httpBody.length);
        httpRespone.setContentType("text/plain");//设置类型
        httpRespone.setCharacterEncoding("UTF-8");//设置编码
  		int expireSec = 0;
        httpRespone.setHeader("Cache-Control", "max-age="+expireSec);
        try {
			httpRespone.getOutputStream().write(httpBody);
			httpRespone.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
