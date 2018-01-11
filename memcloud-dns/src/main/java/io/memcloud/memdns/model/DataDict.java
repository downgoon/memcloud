package io.memcloud.memdns.model;

public class DataDict {

	/** 以字母开始的4-16位字符序列，不能包含中文，可以包含大小写字母，数字，下划线，连接线，连接点*/
	public static final String AppNameRegex = "^[a-zA-Z][-a-zA-Z0-9_.]{3,15}$";
	
	/** 应用描述可以包含中文，英文，连字符等等*/
//	public static final String AppDescRegex = "^[ \t-a-zA-Z0-9_\\u4e00-\\u9fa5]{4,32}$";
	public static final String AppDescRegex = "^.{4,32}$";
	
	public static final String MobileRegex = "^(13|15|18)[0-9]{9}$";
	
	public static final String EmailRegex = "^[0-9a-zA-Z]([-_.~]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,4}$";
	
	private static final String _email = "[0-9a-zA-Z]([-_.~]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,4}";
	private static final String _mobile = "(13|15|18)[0-9]{9}";
	
	public static final String EmailListRegex = "^("+_email+",)*"+_email+"$";
	public static final String MobileListRegex = "^("+_mobile+",)*"+_mobile+"$";
	
}
