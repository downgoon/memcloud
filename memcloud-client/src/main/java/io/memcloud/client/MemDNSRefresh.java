package io.memcloud.client;

import net.sf.json.JSONObject;

public class MemDNSRefresh implements IMemDNSRefresh {

	
	@Override
	public MemDNSLookup refreshDNS(String appid) {
		String api = apiAddr(appid);
		String jsonText = HttpUtil.doGet(api, "UTF-8");

		JSONObject jsonObj = JSONObject.fromObject(jsonText);
		if (jsonObj.getInt("status") != 200) {
			return null;
		}
		JSONObject attachment = jsonObj.getJSONObject("attachment");
		if (attachment == null) {
			return null;
		}
		MemDNSLookup lookup = new MemDNSLookup();
		lookup.setVersion(attachment.getInt("version"));
		lookup.setMemGroup(MemShard.conf2group(attachment.getString("groupText")));
		lookup.setTimestamp(attachment.getLong("timestamp"));
		lookup.setTTLSecond(attachment.getInt("ttlSecond"));
		return lookup;
	}

	protected String apiAddr(String appid) {
		String memdns = System.getProperty("memcloud.memdns");
		
		String template = "/memcloud/dns/%s.json";
		if (memdns.startsWith("http:")) {
			template =  memdns + template;
		} else {
			template = "http://" + memdns + template;
		}
		  
		return String.format(template, appid);
	} 
}
