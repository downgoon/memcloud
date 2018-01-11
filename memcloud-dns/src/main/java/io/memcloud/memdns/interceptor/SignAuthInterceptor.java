//package io.memcloud.memdns.interceptor;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.struts2.ServletActionContext;
//import org.apache.struts2.StrutsStatics;
//
//import com.opensymphony.xwork2.ActionContext;
//import com.opensymphony.xwork2.ActionInvocation;
//import com.github.downgoon.jresty.commons.utils.DynamicProperties;
//import com.github.downgoon.jresty.commons.utils.MD5;
//
//import com.github.downgoon.jresty.rest.model.UnifiedResponse;
//
///** 访问时需要签名的限制 */
//public class SignAuthInterceptor extends BaseInterceptor implements API4InternalAccess {
//
//	private static final long serialVersionUID = 6799698203360986053L;
//
//	private String merchantKey = "merchant"; 
//	
//	private String signKey = "sign";
//	
//	private Map<String,String> merchantSignMapping;
//	
//	/** 从默认的properties配置文件中提取*/
//	private String merchantlistprefix;
//	
//	@Override
//	protected String doIntercept(ActionInvocation invocation) throws Exception {
//		String merchant = getParam(merchantKey);
//		String signRemote = getParam(signKey);
//		
//		UnifiedResponse up = new UnifiedResponse();
//		if(StringUtils.isEmpty(merchant) || StringUtils.isEmpty(signRemote)) {
//			up.setStatus(RC_DENY_SIGN);
//			up.setMessage("需签名授权");
//			up.setDebug(getRefNum4Log(), "服务访问需要验签，缺少输入参数："+merchantKey+"或"+signKey);
//			return REST("DENY",up);
//		}
//		
//		String baseStr = null;
//		try {
//			baseStr = genBaseString4MerchantSign(merchant);
//		}catch(IllegalArgumentException iae) {
//			up.setStatus(RC_DENY_SIGN);
//			up.setMessage("验签失败，缺少必选参数");
//			up.setDebug(getRefNum4Log(), iae.getMessage());
//			return REST("DENY",up);
//		}
//		
//		if(StringUtils.isEmpty(baseStr)) {//表示merchant不在允许范围内
//			up.setStatus(RC_DENY_SIGN);
//			up.setMessage("验签失败");
//			up.setDebug(getRefNum4Log(), "验签失败，商户号："+merchant+" 未分配");
//			return REST("DENY",up);
//		}
//		
//		HttpServletRequest httpRequest = (HttpServletRequest)ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
//		String signLocal = null;
//		if(StringUtils.isEmpty(httpRequest.getCharacterEncoding())) {
//			signLocal = MD5.MD5Encode(baseStr);
//		} else {
//			//样例： http://passport.weiguan.com/passport/kik-imsi.xml?expire=31536000&merchant=kik&ClientCharset=UTF-8&client=Android&sign=f5b80fd580f0cda2ab3ef6254ff5f443&pwd=123456&tm=1300873701465&imsi=460011010670034&nick=%E8%B7%AF%E4%BA%BA%E7%94%B2
//			signLocal = MD5.MD5Encode(baseStr, httpRequest.getCharacterEncoding());//2011-03-23 编码协商，同时要支持签名编码，签名在编码前
//		}
//		
//		if(! StringUtils.equalsIgnoreCase(signLocal, signRemote)) {//验签失败
//			up.setStatus(RC_DENY_SIGN);
//			up.setMessage("验签失败");
//			up.setDebug(getRefNum4Log(), "验签不匹配：remote="+signRemote+(debugMode?",local="+signLocal : ""));
//			log.warn("验签不匹配：remote="+signRemote+",local="+signLocal+",refnum="+getRefNum4Log());
//			return REST("DENY",up);
//		}
//		return invocation.invoke();
//	}
//
//	
//	@SuppressWarnings("rawtypes")
//	private String getParam(String key) {
//		Map qsParams = ServletActionContext.getRequest().getParameterMap();
//		Object value = qsParams.get(key);
//		if(value == null) {
//			return null;
//		}
//		if(value instanceof String[] && 
//				((String[])value).length == 1) {
//			return  ((String[])value)[0];
//		} else {
//			return value.toString();
//		}
//	}
//	
//	/** 
//	 *  生成待签名的原文
//	 *  @return	返回签名的BaseString，如果为NULL，表示merchant当前不允许；
//	 * */
//	private String genBaseString4MerchantSign(String merchant) {
//		StringBuffer sb = new StringBuffer();
//		String md5separator = "null";//表示拼接字段时不需要分隔符
//		String md5fields = null;
//		if(merchantSignMapping!=null) {//本地覆盖
//			md5fields = merchantSignMapping.get(merchant);
//			
//		} else if(merchantlistprefix!=null){
//			md5fields = DynamicProperties.parseRefProperty(merchantlistprefix+"."+merchant);
//		}
//		
//		if(StringUtils.isEmpty(md5fields)) {
//			return null;
//		}
//		StringBuffer requiredNote = new StringBuffer();
//		String[] fields = md5fields.split(",");
//		for(int i=0;i<fields.length;i++) {
//			if(fields[i].startsWith("${") && fields[i].endsWith("}")) {//变量（必选）
//				String k = fields[i].substring("${".length(), fields[i].length()-"}".length());
//				String v = getParam(k);
//				if(v!=null) {//必选变量：如果配置的需要参与签名的参数，没有传递，则拒绝访问
//					sb.append(v);
//					if(!md5separator.equalsIgnoreCase("null")) {
//						sb.append(md5separator);
//					} 
//				} else {
//					requiredNote.append("缺少参数："+k);
//				}
//			}
//			else if(fields[i].startsWith("$[") && fields[i].endsWith("]")) {//常量
//				sb.append(fields[i].substring("$[".length(), fields[i].length()-"]".length()));
//				if(!md5separator.equalsIgnoreCase("null")) {
//					sb.append(md5separator);
//				}
//			} 
//			else if(fields[i].startsWith("$(") && fields[i].endsWith(")")) {//变量（可选）
//				String k = fields[i].substring("$(".length(), fields[i].length()-")".length());
//				String v = getParam(k);
//				if(v!=null) {//可选变量：如果配置的需要参与签名的参数，没有传递，则该字段不参与签名
//					sb.append(v);
//					if(!md5separator.equalsIgnoreCase("null")) {
//						sb.append(md5separator);
//					} 
//				} else {
//					//忽略可选字段
//				}
//			}
//			else {
//				log.warn("忽略商户:"+merchant+"的MD5序列字段："+fields[i]);
//			}
//		}
//		if(requiredNote!=null && requiredNote.length() > 0) {
//			throw new IllegalArgumentException(requiredNote.toString());
//		}
//		return sb.toString();
//	}
//
//	public String getMerchantKey() {
//		return merchantKey;
//	}
//
//	public void setMerchantKey(String merchantKey) {
//		this.merchantKey = merchantKey;
//	}
//
//	public Map<String, String> getMerchantSignMapping() {
//		return merchantSignMapping;
//	}
//
//	public void setMerchantSignMapping(Map<String, String> merchantSignMapping) {
//		this.merchantSignMapping = merchantSignMapping;
//	}
//
//	public String getSignKey() {
//		return signKey;
//	}
//
//	public void setSignKey(String signKey) {
//		this.signKey = signKey;
//	}
//
//
//	public String getMerchantlistprefix() {
//		return merchantlistprefix;
//	}
//
//
//	public void setMerchantlistprefix(String merchantlistprefix) {
//		this.merchantlistprefix = merchantlistprefix;
//	}
//	
//	private boolean debugMode = false;
//	public boolean isDebugMode() {
//		return debugMode;
//	}
//	public void setDebugMode(boolean debugMode) {
//		this.debugMode = debugMode;
//	}
//	
//}
