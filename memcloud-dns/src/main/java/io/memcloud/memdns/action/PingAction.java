package io.memcloud.memdns.action;

import com.github.downgoon.jresty.rest.model.UnifiedResponse;
import com.github.downgoon.jresty.rest.view.DefaultHttpHeaders;

public class PingAction extends BaseAction {

	private static final long serialVersionUID = 6572622442036315342L;

	private UnifiedResponse response = new UnifiedResponse();
	
	@Override
	public UnifiedResponse getModel() {
		return this.response;
	}
	
	public String debug()   {
		//CASE 1: 正常情况（没有任何异常）
//		return REST(RP_NOHTML);
		
		//CASE 2: 运行时异常
//		throw new RuntimeException("观察运行时异常的处理情况");
		
		//CASE 3: 非运行时异常（Checked Exception）
		//在标题上跑出 throws Exception
//		throw new Exception("观察【非】运行时异常的处理情况");
		return REST("debug");
	}

	@Override
	public String index() throws Exception {
		response.setDebug(getRefNum4Log(), "系统运行正常");
		log.info(getRefNum4Log()+":"+"系统运行正常");
		//SOHU Page Status Number 是给页面静态化使用的私有协议，PSN由：M/N构成，其中M<=N；
		//如果M<N表示页面生成残缺；如果M=N表示生成正常。
		return REST(new DefaultHttpHeaders(SUCCESS).withStatus(200).addHeader("PSN", "1/1"), false);
	}
	
}
